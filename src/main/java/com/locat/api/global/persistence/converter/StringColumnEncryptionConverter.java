package com.locat.api.global.persistence.converter;

import com.locat.api.global.exception.custom.InternalProcessingException;
import com.locat.api.global.utils.RandomGenerator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Value;

@Converter
public class StringColumnEncryptionConverter implements AttributeConverter<String, String> {

  /** 암호화 알고리즘: AES/GCM/NoPadding */
  private static final String ENCRYPTION_TRANSFORM = "AES/GCM/NoPadding";

  /** AES 키의 길이: 32 바이트 */
  private static final int AES_KEY_LENGTH = 32;

  /** 초기화 벡터의 길이: 12 바이트로 고정 */
  private static final int INITIAL_VECTOR_LENGTH = 12;

  private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

  private SecretKeySpec secretKeySpec;

  @Value("${service.key.encryption}")
  private String encryptionKey;

  @PostConstruct
  public void init() {
    this.secretKeySpec = generateAESKey(this.encryptionKey);
  }

  /**
   * 문자열을 AES/GCM 방식으로 암호화, Base64로 인코딩하여 반환합니다.
   *
   * @param attribute 암호화할 원본 문자열
   * @return 암호화된 문자열, 원본 데이터가 {@code null}인 경우 {@code null} 반환
   * @throws InternalProcessingException 암호화 중 오류가 발생한 경우
   */
  @Override
  public String convertToDatabaseColumn(String attribute) {
    if (attribute == null) {
      return null;
    }
    try {
      final byte[] initialVector = RandomGenerator.generateRandomBytes(INITIAL_VECTOR_LENGTH);

      Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, this.secretKeySpec, initialVector);
      byte[] encryptedData = cipher.doFinal(attribute.getBytes(DEFAULT_ENCODING));
      byte[] encryptedWithIv =
          ByteBuffer.allocate(initialVector.length + encryptedData.length)
              .put(initialVector)
              .put(encryptedData)
              .array();

      return Base64.toBase64String(encryptedWithIv);
    } catch (GeneralSecurityException e) {
      throw new InternalProcessingException("Failed to encrypt data.", e);
    }
  }

  /**
   * 데이터베이스에서 가져온 암호화된 문자열을 복호화하여 원본 문자열로 변환합니다.<br>
   *
   * <p>설명
   * <li>암호화된 데이터는 Base64 인코딩 처리 & 초기화 벡터와 실제 데이터가 합쳐진 상태
   * <li>복호화를 위해 초기화 벡터와 암호화된 데이터를 분리해 복호화 진행
   *
   * @param dbData 복호화할 암호화된 데이터
   * @return 복호화된 원본 문자열, 데이터가 {@code null}인 경우 {@code null} 반환
   * @throws InternalProcessingException 복호화 중 오류가 발생한 경우
   */
  @Override
  public String convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    try {
      byte[] decodedData = Base64.decode(dbData);
      ByteBuffer byteBuffer = ByteBuffer.wrap(decodedData);

      byte[] initialVector = new byte[INITIAL_VECTOR_LENGTH];
      byteBuffer.get(initialVector);

      byte[] encryptedData = new byte[byteBuffer.remaining()];
      byteBuffer.get(encryptedData);

      Cipher cipher = initCipher(Cipher.DECRYPT_MODE, this.secretKeySpec, initialVector);
      byte[] decryptedData = cipher.doFinal(encryptedData);
      return new String(decryptedData, DEFAULT_ENCODING);
    } catch (GeneralSecurityException e) {
      throw new InternalProcessingException("Failed to decrypt data.", e);
    }
  }

  /**
   * AES/GCM 알고리즘을 초기화합니다.
   *
   * @param mode 암호화 또는 복호화 모드
   * @param secretKeySpec AES 알고리즘을 위한 SecretKeySpec
   * @param iv 초기화 벡터
   * @return 초기화된 Cipher 객체
   */
  private static Cipher initCipher(final int mode, SecretKeySpec secretKeySpec, final byte[] iv) {
    try {
      Cipher cipher = Cipher.getInstance(ENCRYPTION_TRANSFORM);
      final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
      cipher.init(mode, secretKeySpec, gcmParameterSpec);
      return cipher;
    } catch (GeneralSecurityException e) {
      throw new InternalProcessingException("Failed to initialize cipher.", e);
    }
  }

  /**
   * Key 문자열을 AES 알고리즘을 위한 {@link SecretKeySpec}으로 변환합니다.
   *
   * @param key 변환할 Key 문자열
   * @return AES 알고리즘을 위한 SecretKeySpec
   */
  private static SecretKeySpec generateAESKey(final String key) {
    byte[] keyBytes = key.getBytes(DEFAULT_ENCODING);
    final byte[] finalKey = new byte[AES_KEY_LENGTH];

    for (int i = 0; i < finalKey.length; i++) {
      finalKey[i] = keyBytes[i % keyBytes.length];
    }

    return new SecretKeySpec(finalKey, "AES");
  }
}