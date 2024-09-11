package com.locat.api.domain.user.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 사용자 회원가입 요청 DTO
 *
 * @param oAuthId OAuth2 ID
 * @param isTermsOfServiceAgreed 이용약관 동의 여부
 * @param isPrivacyPolicyAgreed 개인정보 처리방침 동의 여부
 * @param isLocationPolicyAgreed 위치정보 이용약관 동의 여부
 * @param isMarketingPolicyAgreed 마케팅 정보 수신 동의 여부
 */
public record UserRegisterRequest(
    @NotEmpty String oAuthId,
    @AssertTrue(message = "You MUST agree to the terms of service.") Boolean isTermsOfServiceAgreed,
    @AssertTrue(message = "You MUST agree to the privacy policy.") Boolean isPrivacyPolicyAgreed,
    @AssertTrue(message = "You MUST agree to the location policy.") Boolean isLocationPolicyAgreed,
    @NotNull Boolean isMarketingPolicyAgreed) {}
