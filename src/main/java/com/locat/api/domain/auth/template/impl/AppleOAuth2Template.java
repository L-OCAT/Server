package com.locat.api.domain.auth.template.impl;

import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.auth.template.AbstractOAuth2Template;
import com.locat.api.domain.auth.template.OAuth2Properties;
import com.locat.api.domain.user.dto.OAuth2ProviderTermsAgreementDto;
import com.locat.api.domain.user.dto.OAuth2UserInfoDto;
import com.locat.api.infrastructure.redis.OAuth2ProviderTokenRepository;
import org.springframework.stereotype.Component;

@Component
public class AppleOAuth2Template extends AbstractOAuth2Template {

  private static final String NOT_IMPLEMENTED_YET = "Not implemented yet";

  public AppleOAuth2Template(
      OAuth2Properties oAuth2Properties, OAuth2ProviderTokenRepository providerTokenRepository) {
    super(oAuth2Properties, providerTokenRepository);
  }

  @Override
  public OAuth2ProviderToken issueToken(String code) {
    throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
  }

  @Override
  public OAuth2UserInfoDto fetchUserInfo(String accessToken) {
    throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
  }

  @Override
  public OAuth2UserInfoDto fetchUserInfoByAdmin(String userOAuthId) {
    throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
  }

  @Override
  public OAuth2ProviderTermsAgreementDto fetchTermsAgreement(String accessToken) {
    throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
  }

  @Override
  public OAuth2ProviderTermsAgreementDto fetchTermsAgreementByAdmin(String... userOAuthIds) {
    throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
  }
}
