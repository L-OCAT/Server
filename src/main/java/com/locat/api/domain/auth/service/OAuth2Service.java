package com.locat.api.domain.auth.service;

import com.locat.api.domain.user.entity.OAuth2ProviderType;

public interface OAuth2Service {

  /**
   * OAuth2 인증을 수행합니다.
   *
   * @param provider OAuth2 제공자({@link OAuth2ProviderType})
   * @param code 인가 코드
   * @return OAuth2 제공자가 관리하는 고유 ID
   */
  String authenticate(OAuth2ProviderType provider, String code);

  Boolean isAuthenticated(final String oAuthId);
}
