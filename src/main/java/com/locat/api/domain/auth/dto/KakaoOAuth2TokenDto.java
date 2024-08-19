package com.locat.api.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

/**
 * Kakao OAuth2 Response
 *
 * @param tokenType 토큰 타입, {@code Bearer}로 고정
 * @param accessToken 액세스 토큰
 * @param idToken ID 토큰
 * @param accessTokenExpiresIn 액세스 토큰 만료 시간(초)
 * @param refreshToken 리프레시 토큰
 * @param refreshTokenExpiresIn 리프레시 토큰 만료 시간(초)
 * @param scope 인증된 사용자의 조회 권한 범위
 */
public record KakaoOAuth2TokenDto(
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("id_token") @Nullable String idToken,
    @JsonProperty("expires_in") Integer accessTokenExpiresIn,
    @JsonProperty("refresh_token") String refreshToken,
    @JsonProperty("refresh_token_expires_in") Integer refreshTokenExpiresIn,
    @JsonProperty("scope") @Nullable String scope)
    implements OAuth2ProviderTokenDto {

  @Override
  public String getAccessToken() {
    return this.accessToken();
  }

  @Override
  public String getRefreshToken() {
    return this.refreshToken();
  }

  @Override
  public Integer getAccessTokenExpiresIn() {
    return this.accessTokenExpiresIn();
  }

  @Override
  public Integer getRefreshTokenExpiresIn() {
    return this.refreshTokenExpiresIn();
  }
}
