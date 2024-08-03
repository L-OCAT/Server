package com.locat.api.domain.user.dto;

import com.locat.api.domain.user.entity.OAuth2ProviderType;

import java.util.List;

public record KakaoUserTermsAgreementDto(String id, List<AgreementDetails> agreementDetails)
    implements OAuth2ProviderTermsAgreementDto {

  @Override
  public String getId() {
    return this.id();
  }

  @Override
  public OAuth2ProviderType getProviderType() {
    return OAuth2ProviderType.KAKAO;
  }

  @Override
  public List<AgreementDetails> getAgreementDetails() {
    return this.agreementDetails();
  }
}
