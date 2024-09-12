package com.locat.api.domain.user.dto;

import com.locat.api.domain.user.dto.request.UserRegisterRequest;

public record UserRegisterDto(
    String oAuthId,
    boolean isTermsOfServiceAgreed,
    boolean isPrivacyPolicyAgreed,
    boolean isLocationPolicyAgreed,
    boolean isMarketingPolicyAgreed) {

  public static UserRegisterDto fromRequest(UserRegisterRequest request) {
    return new UserRegisterDto(
        request.oAuthId(),
        request.isTermsOfServiceAgreed(),
        request.isPrivacyPolicyAgreed(),
        request.isLocationPolicyAgreed(),
        request.isMarketingPolicyAgreed());
  }
}
