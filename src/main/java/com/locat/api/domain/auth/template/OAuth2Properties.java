package com.locat.api.domain.auth.template;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "security.oauth2.client")
public class OAuth2Properties {

  public static final String KAKAO_ADMIN_KEY = "KakaoAK";
  public static final String KAKAO_GRANT_TYPE = "authorization_code";
  public static final String KAKAO_TARGET_ID_TYPE = "user_id";

  public static final String APPLE_ADMIN_KEY = "APPLE_ADMIN_KEY_HERE";
  public static final String APPLE_GRANT_TYPE = "APPLE_GRANT_TYPE_HERE";

  private String kakaoAdminKey;

  private String kakaoClientId;

  private String kakaoClientSecret;

  private String kakaoRedirectUri;

  private String appleAdminKey;

  private String appleClientId;

  private String appleClientSecret;

  private String appleRedirectUri;
}
