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

  private String kakaoAdminKey;

  private String kakaoClientId;

  private String kakaoClientSecret;

  private String kakaoRedirectUri;

  public static final String APPLE_GRANT_TYPE = "authorization_code";

  public static final String APPLE_ACCESS_TOKEN_HINT = "access_token";

  public static final String APPLE_REFRESH_TOKEN_HINT = "refresh_token";

  private String appleTeamId;

  private String appleClientId;

  private String appleRedirectUri;

  private String appleKeyId;

  private String appleKeyPath;
}
