package com.locat.api.domain.auth.dto.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.locat.api.domain.user.enums.OAuth2ProviderType;

/**
 * 카카오 사용자 정보 DTO
 *
 * @param id 회원번호(카카오에서 발급하는 고유 ID)
 * @param kakaoAccount 카카오 계정 정보
 */
public record KakaoUserInfo(String id, @JsonProperty("kakao_account") KakaoAccount kakaoAccount)
    implements OAuth2UserInfo {

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public String getEmail() {
    return this.kakaoAccount.email;
  }

  @Override
  public OAuth2ProviderType getProvider() {
    return OAuth2ProviderType.KAKAO;
  }

  /**
   * 카카오 계정 정보 (사용자에게 동의 받은 항목만 포함)
   *
   * @param hasEmail 이메일 소유 여부
   * @param isEmailValid 이메일 유효 여부
   * @param isEmailVerified 이메일 인증 여부
   * @param email 사용자 이메일
   */
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record KakaoAccount(
      Boolean hasEmail, Boolean isEmailValid, Boolean isEmailVerified, String email) {}
}
