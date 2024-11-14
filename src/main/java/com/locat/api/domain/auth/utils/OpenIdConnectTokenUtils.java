package com.locat.api.domain.auth.utils;

import com.locat.api.domain.auth.dto.token.AppleIdToken;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.security.exception.AuthenticationException;
import com.locat.api.global.security.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class OpenIdConnectTokenUtils {

  private static final int JWT_PARTS_COUNT = 3;
  private static final String HEADER_KEY_ID = "kid";
  private static final Pattern SPLITTER = Pattern.compile("\\.");

  private OpenIdConnectTokenUtils() {
    // Utility class
  }

  public static AppleIdToken parse(String idToken, String modulus, String exponent) {
    Claims body = parseTokenClaims(idToken, modulus, exponent).getBody();
    return AppleIdToken.fromJwt(body);
  }

  public static String parseKeyIdHeader(
      String idToken, String expectedIssuer, String expectedAudience) {
    return Jwts.parserBuilder()
        .requireIssuer(expectedIssuer)
        .requireAudience(expectedAudience)
        .build()
        .parseClaimsJwt(extractJwtHeaderAndPayload(idToken))
        .getHeader()
        .get(HEADER_KEY_ID)
        .toString();
  }

  private static Jws<Claims> parseTokenClaims(String token, String modulus, String exponent) {
    return Jwts.parserBuilder()
        .setSigningKey(generateRSAPublicKey(modulus, exponent))
        .build()
        .parseClaimsJws(token);
  }

  /** Modulus와 Exponent를 이용하여 RSA 공개키를 생성합니다. */
  private static Key generateRSAPublicKey(String base64Modulus, String base64Exponent) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      byte[] decodedModulus = Base64.getUrlDecoder().decode(base64Modulus);
      byte[] decodedExponent = Base64.getUrlDecoder().decode(base64Exponent);
      BigInteger modulus = new BigInteger(1, decodedModulus);
      BigInteger exponent = new BigInteger(1, decodedExponent);

      RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
      return keyFactory.generatePublic(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      log.error("Failed to generate RSA public key / Reason: {}", e.getMessage());
      throw new AuthenticationException(ApiExceptionType.INTERNAL_SERVER_ERROR);
    }
  }

  /** JWT 토큰의 Header와 Payload를 추출합니다. */
  private static String extractJwtHeaderAndPayload(String idToken) {
    String[] parts = SPLITTER.split(idToken);
    if (parts.length != JWT_PARTS_COUNT) {
      throw new TokenException();
    }
    return parts[0] + "." + parts[1] + ".";
  }
}
