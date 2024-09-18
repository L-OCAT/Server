package com.locat.api.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.locat.api.domain.user.entity.OAuth2ProviderType;

public record KakaoUserInfoDto(String id, @JsonProperty("kakao_account") KakaoAccount kakaoAccount)
    implements OAuth2UserInfoDto {

  @Override
  public String getId() {
    return this.id();
  }

  @Override
  public String getEmail() {
    return this.kakaoAccount.email();
  }

  @Override
  public OAuth2ProviderType getProvider() {
    return OAuth2ProviderType.KAKAO;
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record KakaoAccount(
      Boolean hasEmail, Boolean isEmailValid, Boolean isEmailVerified, String email) {}
}
