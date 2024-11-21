package com.locat.api.domain.auth.dto.token;

/** OAuth2 Provider별 응답을 담는 인터페이스 */
public sealed interface OAuth2ProviderTokenDto permits AppleOAuth2TokenDto, KakaoOAuth2TokenDto {

  /**
   * OAuth2 제공자가 발급한 접근 토큰(access token)을 반환한다.
   *
   * @return 접근 토큰
   */
  String getAccessToken();

  /**
   * OAuth2 제공자가 발급한 갱신 토큰(refresh token)을 반환한다.
   *
   * @return 갱신 토큰
   */
  String getRefreshToken();

  /**
   * OAuth2 제공자가 발급한 ID 토큰(ID token)을 반환한다.
   *
   * @return ID 토큰
   */
  String getIdToken();

  /**
   * OAuth2 제공자가 발급한 접근 토큰의 만료 시간을 반환한다.
   *
   * @return 접근 토큰 만료 시간(초)
   */
  Integer getAccessTokenExpiresIn();

  /**
   * OAuth2 제공자가 발급한 갱신 토큰의 만료 시간을 반환한다.
   *
   * @return 갱신 토큰 만료 시간(초)
   */
  Integer getRefreshTokenExpiresIn();
}
