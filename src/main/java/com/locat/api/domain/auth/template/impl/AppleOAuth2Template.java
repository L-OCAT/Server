package com.locat.api.domain.auth.template.impl;

import com.locat.api.domain.auth.dto.OAuth2ProviderTokenDto;
import com.locat.api.domain.auth.dto.OAuth2UserInfo;
import com.locat.api.domain.auth.dto.token.AppleIdToken;
import com.locat.api.domain.auth.dto.token.OAuth2ProviderJsonWebKey;
import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.auth.template.AbstractOAuth2Template;
import com.locat.api.domain.auth.template.OAuth2Properties;
import com.locat.api.domain.auth.utils.AppleClientSecretProvider;
import com.locat.api.domain.auth.utils.OpenIdConnectTokenUtils;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.global.exception.InternalProcessingException;
import com.locat.api.infrastructure.external.AppleOAuth2Client;
import com.locat.api.infrastructure.redis.OAuth2ProviderTokenRepository;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AppleOAuth2Template extends AbstractOAuth2Template {

  private final AppleOAuth2Client appleOAuth2Client;

  public AppleOAuth2Template(
      OAuth2Properties oAuth2Properties,
      AppleOAuth2Client appleOAuth2Client,
      OAuth2ProviderTokenRepository providerTokenRepository) {
    super(oAuth2Properties, providerTokenRepository);
    this.appleOAuth2Client = appleOAuth2Client;
  }

  @Override
  public OAuth2ProviderToken issueToken(String code) {
    OAuth2Properties.Apple appleProperties = this.oAuth2Properties.apple();
    final String clientSecret = AppleClientSecretProvider.create(appleProperties);
    final OAuth2ProviderTokenDto appleOAuth2TokenDto =
        this.appleOAuth2Client.issueOrRenewToken(
            appleProperties.clientId(),
            clientSecret,
            code,
            OAuth2Properties.Apple.GRANT_TYPE,
            appleProperties.redirectUri(),
            null);
    final String idToken = appleOAuth2TokenDto.getIdToken();
    OAuth2ProviderJsonWebKey jsonWebKey = this.getMatchingJsonWebKey(idToken);
    AppleIdToken parsedIdToken =
        OpenIdConnectTokenUtils.parse(idToken, jsonWebKey.n(), jsonWebKey.e());
    return this.providerTokenRepository.save(
        OAuth2ProviderToken.from(
            OAuth2ProviderType.APPLE, parsedIdToken.getId(), appleOAuth2TokenDto));
  }

  @Override
  public OAuth2UserInfo fetchUserInfo(String oAuthId) {
    final String idToken = super.fetchToken(oAuthId).getIdToken();
    OAuth2ProviderJsonWebKey jsonWebKey = this.getMatchingJsonWebKey(idToken);
    return OpenIdConnectTokenUtils.parse(idToken, jsonWebKey.n(), jsonWebKey.e());
  }

  @Override
  @Transactional
  public void withdrawal(String userOAuthId) {
    final String clientSecret = AppleClientSecretProvider.create(super.oAuth2Properties.apple());
    OAuth2ProviderToken providerToken = super.fetchToken(userOAuthId);
    this.revokeAccessToken(clientSecret, providerToken.getAccessToken());
    this.revokeRefreshToken(clientSecret, providerToken.getRefreshToken());
    this.providerTokenRepository.delete(providerToken);
  }

  /** Apple의 공개키 중, idToken 암호화 방식과 일치하는 공개키를 찾아 반환합니다. */
  private OAuth2ProviderJsonWebKey getMatchingJsonWebKey(String idToken) {
    final String keyId =
        OpenIdConnectTokenUtils.parseKeyIdHeader(
            idToken, OAuth2Properties.Apple.AUDIENCE, this.oAuth2Properties.apple().clientId());
    List<OAuth2ProviderJsonWebKey> jsonWebKeys = this.appleOAuth2Client.getPublicKeys().keys();
    return jsonWebKeys.stream()
        .filter(jwk -> jwk.kid().equals(keyId))
        .findFirst()
        .orElseThrow(
            () -> new InternalProcessingException("Cannot Process JWT: No matching algorithm"));
  }

  private void revokeAccessToken(String clientSecret, String accessToken) {
    this.appleOAuth2Client.revokeToken(
        super.oAuth2Properties.apple().clientId(),
        clientSecret,
        accessToken,
        OAuth2Properties.Apple.ACCESS_TOKEN_HINT);
  }

  private void revokeRefreshToken(String clientSecret, String refreshToken) {
    this.appleOAuth2Client.revokeToken(
        super.oAuth2Properties.apple().clientId(),
        clientSecret,
        refreshToken,
        OAuth2Properties.Apple.REFRESH_TOKEN_HINT);
  }
}
