package com.locat.api.domain.auth.service.impl;

import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.auth.service.OAuth2Service;
import com.locat.api.domain.auth.template.OAuth2Template;
import com.locat.api.domain.auth.template.OAuth2TemplateFactory;
import com.locat.api.domain.user.entity.OAuth2ProviderType;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.UserOAuth;
import com.locat.api.domain.user.service.UserRegistrationService;
import com.locat.api.global.auth.jwt.JwtProvider;
import com.locat.api.global.auth.jwt.LocatTokenDto;
import com.locat.api.infrastructure.repository.user.UserOAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

  private final JwtProvider jwtProvider;
  private final UserRegistrationService userRegistrationService;
  private final UserOAuthRepository userOAuthRepository;
  private final OAuth2TemplateFactory oAuth2TemplateFactory;

  @Override
  public LocatTokenDto authenticate(OAuth2ProviderType provider, String code) {
    OAuth2Template oAuth2Template = oAuth2TemplateFactory.getByType(provider);
    OAuth2ProviderToken providerToken = oAuth2Template.issueToken(code);
    return this.createLocatToken(providerToken);
  }

  private LocatTokenDto createLocatToken(OAuth2ProviderToken providerToken) {
    final String oAuthId = providerToken.getId();
    User user =
        this.userOAuthRepository
            .findByOauthId(providerToken.getId())
            .map(UserOAuth::getUser)
            .orElseGet(() -> this.userRegistrationService.registerByOAuth(oAuthId));
    return jwtProvider.create(user.getEmail());
  }
}
