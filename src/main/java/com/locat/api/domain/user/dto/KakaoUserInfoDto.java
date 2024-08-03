package com.locat.api.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfoDto(String id, KakaoAccount kakaoAccount) implements OAuth2UserInfoDto {

  @Override
  public String getId() {
    return this.id();
  }

  @Override
  public String getEmail() {
    return this.kakaoAccount.email();
  }

  public record KakaoAccount(
      @JsonProperty("has_email") Boolean hasEmail,
      @JsonProperty("is_email_valid") Boolean isEmailValid,
      @JsonProperty("is_email_verified") Boolean isEmailVerified,
      String email) {}
}
