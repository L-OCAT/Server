package com.locat.api.domain.auth.dto.token;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.locat.api.domain.auth.dto.OAuth2ProviderTokenDto;

/**
 * Apple OAuth2 Response
 *
 * @param tokenType 토큰 타입, {@code Bearer}로 고정
 * @param accessToken 액세스 토큰
 * @param idToken ID 토큰
 * @param refreshToken 리프레시 토큰
 * @param expiresIn 액세스 토큰 만료 시간(초)
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AppleOAuth2TokenDto(
    String tokenType, String accessToken, String idToken, String refreshToken, Integer expiresIn)
    implements OAuth2ProviderTokenDto {
  @Override
  public String getAccessToken() {
    return this.accessToken;
  }

  @Override
  public String getRefreshToken() {
    return this.refreshToken;
  }

  @Override
  public String getIdToken() {
    return this.idToken;
  }

  @Override
  public Integer getAccessTokenExpiresIn() {
    return this.expiresIn;
  }

  @Override
  public Integer getRefreshTokenExpiresIn() {
    return Integer.MAX_VALUE;
  }
}
