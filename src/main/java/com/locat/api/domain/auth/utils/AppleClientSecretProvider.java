package com.locat.api.domain.auth.utils;

import static com.locat.api.domain.auth.template.impl.AppleOAuth2Template.APPLE_AUDIENCE;

import com.locat.api.domain.auth.template.OAuth2Properties;
import com.locat.api.global.auth.AuthenticationException;
import com.locat.api.global.exception.ApiExceptionType;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public final class AppleClientSecretProvider {

  private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.ES256;

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  private AppleClientSecretProvider() {
    // Utility class
  }

  /**
   * Apple OAuth2 인증 요청에 사용할 Client Secret을 생성합니다.
   *
   * @param oAuth2Properties OAuth2 설정 정보
   * @return 생성된 Client Secret
   *     <ul>
   *       <li>JWS 헤더를 설정합니다. (알고리즘과 키 ID 포함)
   *       <li>JWT의 클레임을 설정합니다. (Issuer, Subject, Audience 등)
   *       <li>Apple 개인 키를 이용하여 JWT에 서명하고, 문자열 형태의 토큰을 생성합니다.
   *       <li>getAuthKey 메서드를 통해 Apple의 개인 키를 파일에서 읽어옵니다.
   *       <li>읽어온 키를 PrivateKey 객체로 변환합니다.
   *     </ul>
   */
  public static String create(OAuth2Properties oAuth2Properties) {
    Map<String, Object> jwsHeader =
        Map.of(
            JwsHeader.ALGORITHM, SIGNATURE_ALGORITHM.getValue(),
            JwsHeader.KEY_ID, oAuth2Properties.getAppleKeyId());
    return Jwts.builder()
        .setHeader(jwsHeader)
        .setIssuer(oAuth2Properties.getAppleTeamId())
        .setIssuedAt(new Date())
        .setSubject(oAuth2Properties.getAppleClientId())
        .setAudience(APPLE_AUDIENCE)
        .setExpiration(Date.from(Instant.now().plus(3, ChronoUnit.MONTHS)))
        .signWith(getAuthKey(oAuth2Properties.getAppleKeyPath()), SIGNATURE_ALGORITHM)
        .compact();
  }

  private static PrivateKey getAuthKey(String appleKeyPath) {
    ClassPathResource resource = new ClassPathResource(appleKeyPath);

    try (PemReader pemReader = new PemReader(new InputStreamReader(resource.getInputStream()))) {
      PemObject pemObject = pemReader.readPemObject();
      byte[] keyBytes = pemObject.getContent();

      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory kf = KeyFactory.getInstance("EC");

      return kf.generatePrivate(spec);
    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      log.error("Failed to read Apple private key / Reason: {}", e.getMessage());
      throw new AuthenticationException(ApiExceptionType.INTERNAL_SERVER_ERROR);
    }
  }
}
