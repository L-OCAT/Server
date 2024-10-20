package com.locat.api.domain.auth.dto.token;

/** OAuth2 Provider별 응답을 담는 인터페이스 */
public interface OAuth2ProviderTokenDto {

  String getAccessToken();

  String getRefreshToken();

  String getIdToken();

  Integer getAccessTokenExpiresIn();

  Integer getRefreshTokenExpiresIn();
}
