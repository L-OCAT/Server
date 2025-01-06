package com.locat.api.domain.user.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 사용자 회원가입 요청 DTO
 *
 * @param oAuthId OAuth2 ID
 * @param nickname 닉네임
 * @param isTermsOfServiceAgreed 이용약관 동의 여부
 * @param isPrivacyPolicyAgreed 개인정보 처리방침 동의 여부
 * @param isLocationPolicyAgreed 위치정보 이용약관 동의 여부
 * @param isMarketingPolicyAgreed 마케팅 정보 수신 동의 여부
 */
public record UserRegisterRequest(
    @NotEmpty String oAuthId,
    @Pattern(
            regexp = "^(?=[^ㄱ-ㅎㅏ-ㅣ]*$)[가-힣a-zA-Z0-9]{2,12}$",
            message =
                "Invalid nickname. (2-12 characters long and contain only Korean, English, and numbers)")
        String nickname,
    @AssertTrue(message = "You MUST agree to the terms of service") Boolean isTermsOfServiceAgreed,
    @AssertTrue(message = "You MUST agree to the privacy policy") Boolean isPrivacyPolicyAgreed,
    @AssertTrue(message = "You MUST agree to the location policy") Boolean isLocationPolicyAgreed,
    @NotNull Boolean isMarketingPolicyAgreed) {}
