package com.locat.api.domain.auth.dto;

import com.locat.api.domain.user.enums.OAuth2ProviderType;

/** OAuth2 제공자(Apple, Kakao 등)로부터 받은 사용자 정보를 표준화하는 인터페이스 */
public interface OAuth2UserInfo {

  /**
   * 각 제공자가 발급 & 관리하는 사용자 고유 ID을 반환합니다.
   *
   * @return 사용자 고유 ID
   */
  String getId();

  /**
   * 사용자 이메일 주소를 반환합니다.
   *
   * @return 사용자 이메일
   */
  String getEmail();

  /**
   * OAuth2 제공자 타입을 반환합니다.
   *
   * @return OAuth2 제공자 타입
   */
  OAuth2ProviderType getProvider();
}
