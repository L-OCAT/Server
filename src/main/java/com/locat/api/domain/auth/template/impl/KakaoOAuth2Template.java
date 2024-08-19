package com.locat.api.domain.auth.template.impl;

import com.locat.api.domain.auth.dto.OAuth2ProviderTokenDto;
import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.auth.template.AbstractOAuth2Template;
import com.locat.api.domain.auth.template.OAuth2Properties;
import com.locat.api.domain.user.dto.OAuth2ProviderTermsAgreementDto;
import com.locat.api.domain.user.dto.OAuth2UserInfoDto;
import com.locat.api.domain.user.entity.OAuth2ProviderType;
import com.locat.api.infrastructure.external.KakaoOAuth2Client;
import com.locat.api.infrastructure.external.KakaoUserClient;
import com.locat.api.infrastructure.redis.OAuth2ProviderTokenRepository;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class KakaoOAuth2Template extends AbstractOAuth2Template {

  private final KakaoUserClient kakaoUserClient;
  private final KakaoOAuth2Client kakaoOAuth2Client;

  public KakaoOAuth2Template(
      OAuth2Properties oAuth2Properties,
      OAuth2ProviderTokenRepository providerTokenRepository,
      KakaoUserClient kakaoUserClient,
      KakaoOAuth2Client kakaoOAuth2Client) {
    super(oAuth2Properties, providerTokenRepository);
    this.kakaoUserClient = kakaoUserClient;
    this.kakaoOAuth2Client = kakaoOAuth2Client;
  }

  @Override
  public OAuth2ProviderToken issueToken(String code) {
    OAuth2ProviderTokenDto tokenDto =
        this.kakaoOAuth2Client.issueOrRenewToken(
            OAuth2Properties.KAKAO_GRANT_TYPE,
            super.oAuth2Properties.getKakaoClientId(),
            super.oAuth2Properties.getKakaoClientSecret(),
            super.oAuth2Properties.getKakaoRedirectUri(),
            code);
    final String oAuthId = this.kakaoUserClient.getUserInfo(tokenDto.getAccessToken()).id();
    return super.providerTokenRepository.save(
        OAuth2ProviderToken.from(OAuth2ProviderType.KAKAO, oAuthId, tokenDto));
  }

  @Override
  public OAuth2UserInfoDto fetchUserInfo(String accessToken) {
    return this.kakaoUserClient.getUserInfo(accessToken);
  }

  @Override
  public OAuth2UserInfoDto fetchUserInfoByAdmin(String userOAuthId) {
    return this.kakaoUserClient.getUserInfoByAdmin(
        super.oAuth2Properties.getKakaoAdminKey(),
        OAuth2Properties.KAKAO_TARGET_ID_TYPE,
        Long.parseLong(userOAuthId));
  }

  @Override
  public OAuth2ProviderTermsAgreementDto fetchTermsAgreement(String accessToken) {
    return this.kakaoUserClient.fetchTermsAgreement(accessToken);
  }

  @Override
  public OAuth2ProviderTermsAgreementDto fetchTermsAgreementByAdmin(String... userOAuthIds) {
    return this.kakaoUserClient.fetchTermsAgreementByAdmin(
        super.oAuth2Properties.getKakaoAdminKey(),
        OAuth2Properties.KAKAO_TARGET_ID_TYPE,
        Arrays.stream(userOAuthIds).map(Long::parseLong).toArray(Long[]::new));
  }
}
