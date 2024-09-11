package com.locat.api.domain.auth.template;

import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.user.dto.OAuth2UserInfoDto;

public interface OAuth2Template {

  /**
   * 인가 코드로 토큰을 발급합니다.
   *
   * @param code 인가 코드
   * @return 발급된 {@link OAuth2ProviderToken}
   */
  OAuth2ProviderToken issueToken(final String code);

  /**
   * 사용자의 액세스 토큰으로 사용자 정보를 가져옵니다.
   *
   * @param accessToken 액세스 토큰
   * @return 사용자 정보
   */
  OAuth2UserInfoDto fetchUserInfo(final String accessToken);

  /**
   * 관리자 권한으로 사용자의 OAuth ID를 기반으로 사용자 정보를 가져옵니다.
   *
   * @param userOAuthId 조회할 사용자의 OAuth ID
   * @return 사용자 정보
   */
  OAuth2UserInfoDto fetchUserInfoByAdmin(final String userOAuthId);

  /**
   * 회원 탈퇴(OAuth2 연결 끊기, 토큰 삭제) 작업을 수행합니다.
   *
   * @param userOAuthId 사용자의 OAuth ID
   */
  void withdrawal(final String userOAuthId);
}
