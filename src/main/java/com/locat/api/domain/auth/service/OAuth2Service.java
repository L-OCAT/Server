package com.locat.api.domain.auth.service;

import com.locat.api.domain.user.enums.OAuth2ProviderType;

public interface OAuth2Service {

  /**
   * OAuth2 인증을 수행합니다.
   *
   * @param provider OAuth2 제공자({@link OAuth2ProviderType}) 타입
   * @param code 인가 코드
   * @return OAuth 제공자가 관리하는 고유 ID
   */
  String authenticate(OAuth2ProviderType provider, String code);

  /**
   * 사용자가 인증된 상태인지 확인합니다.
   *
   * @param oAuthId OAuth 제공자가 사용자에게 부여한 고유 ID
   * @return 인증 여부
   */
  Boolean isAuthenticated(final String oAuthId);
}
