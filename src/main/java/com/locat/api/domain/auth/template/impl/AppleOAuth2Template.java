package com.locat.api.domain.auth.template.impl;

import com.locat.api.domain.auth.dto.OAuth2UserInfoDto;
import com.locat.api.domain.auth.dto.token.AppleIdToken;
import com.locat.api.domain.auth.dto.token.OAuth2ProviderJsonWebKey;
import com.locat.api.domain.auth.dto.token.OAuth2ProviderTokenDto;
import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.auth.template.AbstractOAuth2Template;
import com.locat.api.domain.auth.template.OAuth2Properties;
import com.locat.api.domain.auth.utils.AppleClientSecretProvider;
import com.locat.api.domain.auth.utils.OpenIDConnectTokenUtils;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.global.exception.InternalProcessingException;
import com.locat.api.infrastructure.external.AppleOAuth2Client;
import com.locat.api.infrastructure.redis.OAuth2ProviderTokenRepository;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AppleOAuth2Template extends AbstractOAuth2Template {

  public static final String APPLE_AUDIENCE = "https://appleid.apple.com";

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
    final String clientSecret = AppleClientSecretProvider.create(super.oAuth2Properties);
    final OAuth2ProviderTokenDto appleOAuth2TokenDto =
        this.appleOAuth2Client.issueOrRenewToken(
            super.oAuth2Properties.getAppleClientId(),
            clientSecret,
            code,
            OAuth2Properties.APPLE_GRANT_TYPE,
            super.oAuth2Properties.getAppleRedirectUri(),
            null);
    final String idToken = appleOAuth2TokenDto.getIdToken();
    OAuth2ProviderJsonWebKey jsonWebKey = this.getMatchingJsonWebKey(idToken);
    AppleIdToken appleIdToken =
        OpenIDConnectTokenUtils.parse(idToken, jsonWebKey.n(), jsonWebKey.e());
    return this.providerTokenRepository.save(
        OAuth2ProviderToken.from(
            OAuth2ProviderType.APPLE, appleIdToken.getId(), appleOAuth2TokenDto));
  }

  @Override
  public OAuth2UserInfoDto fetchUserInfo(String accessToken) {
    final String idToken = super.fetchTokenByAccessToken(accessToken).getIdToken();
    OAuth2ProviderJsonWebKey jsonWebKey = this.getMatchingJsonWebKey(idToken);
    return OpenIDConnectTokenUtils.parse(idToken, jsonWebKey.n(), jsonWebKey.e());
  }

  @Override
  @Transactional
  public void withdrawal(String userOAuthId) {
    final String clientSecret = AppleClientSecretProvider.create(super.oAuth2Properties);
    OAuth2ProviderToken providerToken = super.fetchToken(userOAuthId);
    this.revokeAccessToken(clientSecret, providerToken.getAccessToken());
    this.revokeRefreshToken(clientSecret, providerToken.getRefreshToken());
    this.providerTokenRepository.delete(providerToken);
  }

  /** Apple의 공개키 중, idToken 암호화 방식과 일치하는 공개키를 찾아 반환합니다. */
  private OAuth2ProviderJsonWebKey getMatchingJsonWebKey(String idToken) {
    final String keyId =
        OpenIDConnectTokenUtils.parseKeyIdHeader(
            idToken, APPLE_AUDIENCE, this.oAuth2Properties.getAppleClientId());
    List<OAuth2ProviderJsonWebKey> jsonWebKeys = this.appleOAuth2Client.getPublicKeys().keys();
    return jsonWebKeys.stream()
        .filter(jwk -> jwk.kid().equals(keyId))
        .findFirst()
        .orElseThrow(
            () -> new InternalProcessingException("Cannot Process JWT: No matching algorithm"));
  }

  private void revokeAccessToken(String clientSecret, String accessToken) {
    this.appleOAuth2Client.revokeToken(
        super.oAuth2Properties.getAppleClientId(),
        clientSecret,
        accessToken,
        OAuth2Properties.APPLE_ACCESS_TOKEN_HINT);
  }

  private void revokeRefreshToken(String clientSecret, String refreshToken) {
    this.appleOAuth2Client.revokeToken(
        super.oAuth2Properties.getAppleClientId(),
        clientSecret,
        refreshToken,
        OAuth2Properties.APPLE_REFRESH_TOKEN_HINT);
  }
}
