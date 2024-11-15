package com.locat.api.domain.user.dto.internal;

import com.locat.api.domain.user.dto.request.UserRegisterRequest;

/**
 * 사용자 등록 DTO
 *
 * @param oAuthId OAuth ID
 * @param nickname 닉네임
 * @param isTermsOfServiceAgreed 이용약관 동의 여부
 * @param isPrivacyPolicyAgreed 개인정보 처리방침 동의 여부
 * @param isLocationPolicyAgreed 위치정보 이용약관 동의 여부
 * @param isMarketingPolicyAgreed 마케팅 정보 수신 동의 여부
 */
public record UserRegisterDto(
    String oAuthId,
    String nickname,
    boolean isTermsOfServiceAgreed,
    boolean isPrivacyPolicyAgreed,
    boolean isLocationPolicyAgreed,
    boolean isMarketingPolicyAgreed) {

  public static UserRegisterDto fromRequest(UserRegisterRequest request) {
    return new UserRegisterDto(
        request.oAuthId(),
        request.nickname(),
        request.isTermsOfServiceAgreed(),
        request.isPrivacyPolicyAgreed(),
        request.isLocationPolicyAgreed(),
        request.isMarketingPolicyAgreed());
  }
}
