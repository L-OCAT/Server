package com.locat.api.domain.auth.template;

import com.locat.api.domain.auth.dto.OAuth2UserInfoDto;
import com.locat.api.domain.auth.entity.OAuth2ProviderToken;

public interface OAuth2Template {

  /**
   * 인가 코드로 토큰을 발급합니다.
   *
   * @param code 인가 코드
   * @return 발급된 {@link OAuth2ProviderToken}
   */
  OAuth2ProviderToken issueToken(final String code);

  /**
   * 사용자가 인증되었는지 확인합니다.
   *
   * @param oAuthId 확인할 사용자의 OAuth ID
   * @return 사용자가 인증되었는지 여부
   */
  Boolean isAuthenticated(final String oAuthId);

  /**
   * 사용자의 OAuth ID으로 사용자 정보를 가져옵니다.
   *
   * @param oAuthId 사용자의 OAuth ID
   * @return 사용자 정보
   */
  OAuth2UserInfoDto fetchUserInfo(final String oAuthId);

  /**
   * 회원 탈퇴(OAuth2 연결 끊기, 토큰 삭제) 작업을 수행합니다.
   *
   * @param userOAuthId 사용자의 OAuth ID
   */
  void withdrawal(final String userOAuthId);
}
