package com.locat.api.domain.auth.template.impl;

import com.locat.api.domain.auth.dto.internal.OAuth2UserInfo;
import com.locat.api.domain.auth.dto.token.OAuth2ProviderTokenDto;
import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.auth.template.AbstractOAuth2Template;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.infra.client.http.KakaoOAuth2Client;
import com.locat.api.infra.client.http.KakaoUserClient;
import com.locat.api.infra.redis.OAuth2ProviderTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    OAuth2Properties.Kakao kakaoProperties = this.oAuth2Properties.kakao();
    OAuth2ProviderTokenDto tokenDto =
        this.kakaoOAuth2Client.issueOrRenewToken(
            OAuth2Properties.Kakao.GRANT_TYPE,
            kakaoProperties.clientId(),
            kakaoProperties.clientSecret(),
            kakaoProperties.redirectUri(),
            code);
    final String oAuthId =
        this.kakaoUserClient.getUserInfo(prependBearer(tokenDto.getAccessToken())).id();
    return super.providerTokenRepository.save(
        OAuth2ProviderToken.from(OAuth2ProviderType.KAKAO, oAuthId, tokenDto));
  }

  @Override
  public OAuth2UserInfo fetchUserInfo(String oAuthId) {
    final String accessToken = super.fetchToken(oAuthId).getAccessToken();
    return this.kakaoUserClient.getUserInfo(prependBearer(accessToken));
  }

  @Override
  @Transactional
  public void withdrawal(String userOAuthId) {
    this.kakaoUserClient.withdrawal(userOAuthId);
    this.providerTokenRepository.deleteById(userOAuthId);
  }
}
