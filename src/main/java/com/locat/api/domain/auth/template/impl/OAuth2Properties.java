package com.locat.api.domain.auth.template.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OAuth2 Client Properties 관리 클래스
 *
 * @param kakao Kakao OAuth2 관련 설정
 * @param apple Apple OAuth2 관련 설정
 */
@ConfigurationProperties(prefix = "security.oauth2.client")
public record OAuth2Properties(Kakao kakao, Apple apple) {
  /**
   * Kakao OAuth2 Properties
   *
   * @param adminKey Kakao Admin Key
   * @param clientId Kakao Client ID
   * @param clientSecret Kakao Client Secret
   * @param redirectUri Kakao Redirect URI
   */
  @SuppressWarnings("unused") // AdminKey 관련 상수는 추후 사용될 수 있으므로 "unused" 경고 무시
  public record Kakao(String adminKey, String clientId, String clientSecret, String redirectUri) {

    public static final String ADMIN_KEY_PREFIX = "KakaoAK ";

    public static final String GRANT_TYPE = "authorization_code";

    public static final String TARGET_ID_TYPE = "user_id";
  }

  /**
   * Apple OAuth2 Properties
   *
   * @param teamId Apple Team ID
   * @param clientId Client ID(App ID 또는 Services ID)
   * @param redirectUri Apple Redirect URI
   * @param keyId Apple Key ID
   * @param keyPath Auth Key 파일 경로
   */
  public record Apple(
      String teamId, String clientId, String redirectUri, String keyId, String keyPath) {

    public static final String AUDIENCE = "https://appleid.apple.com";

    public static final String GRANT_TYPE = "authorization_code";

    public static final String ACCESS_TOKEN_HINT = "access_token";

    public static final String REFRESH_TOKEN_HINT = "refresh_token";
  }
}
