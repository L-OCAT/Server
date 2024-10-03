package com.locat.api.global.converter;

import com.locat.api.global.exception.InternalProcessingException;
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

  private Cipher cipher;

  private SecretKeySpec secretKeySpec;

  @Value("${service.encryption.key}")
  private String encryptionKey;

  @PostConstruct
  public void init() {
    try {
      this.cipher = Cipher.getInstance(ENCRYPTION_TRANSFORM);
      this.secretKeySpec = generateAESKey(this.encryptionKey);
    } catch (GeneralSecurityException e) {
      throw new InternalProcessingException("Failed to initialize Cipher.", e);
    }
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
      final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, initialVector);
      this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKeySpec, gcmParameterSpec);

      byte[] encryptedData = this.cipher.doFinal(attribute.getBytes(DEFAULT_ENCODING));
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

      final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, initialVector);
      this.cipher.init(Cipher.DECRYPT_MODE, this.secretKeySpec, gcmParameterSpec);

      byte[] decryptedData = this.cipher.doFinal(encryptedData);
      return new String(decryptedData, DEFAULT_ENCODING);
    } catch (GeneralSecurityException e) {
      throw new InternalProcessingException("Failed to decrypt data.", e);
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
