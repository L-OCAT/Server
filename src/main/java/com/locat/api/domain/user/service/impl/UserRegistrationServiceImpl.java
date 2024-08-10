package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.auth.template.OAuth2Template;
import com.locat.api.domain.auth.template.OAuth2TemplateFactory;
import com.locat.api.domain.user.dto.OAuth2UserInfoDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.UserOAuth;
import com.locat.api.domain.user.service.*;
import com.locat.api.infrastructure.redis.OAuth2ProviderTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {

  private final UserService userService;
  private final UserOAuthService userOAuthService;
  private final UserTermsService userTermsService;
  private final UserSettingService userSettingService;

  private final OAuth2TemplateFactory oAuth2TemplateFactory;
  private final OAuth2ProviderTokenRepository providerTokenRepository;

  @Override
  public User registerByOAuth(String oAuthId) {
    OAuth2ProviderToken token = this.findTokenById(oAuthId);
    OAuth2UserInfoDto userInfo = this.fetchUserInfo(token);
    final User user = User.fromOAuth(userInfo);

    CompletableFuture<Void> userFuture =
        CompletableFuture.runAsync(() -> this.userService.save(user));
    CompletableFuture<Void> userOAuthFuture =
        userFuture.thenRunAsync(() -> this.userOAuthService.save(UserOAuth.from(user, token)));
    CompletableFuture<Void> userSettingFuture =
        userFuture.thenRunAsync(() -> this.userSettingService.registerDefaultSettings(user));
    CompletableFuture<Void> userTermsAgreementFuture =
        userFuture.thenRunAsync(() -> this.userTermsService.registerByOAuth(user, token));

    CompletableFuture.allOf(
            userFuture, userOAuthFuture, userSettingFuture, userTermsAgreementFuture)
        .join();
    return user;
  }

  private OAuth2UserInfoDto fetchUserInfo(OAuth2ProviderToken token) {
    OAuth2Template oAuth2Template = this.oAuth2TemplateFactory.getByType(token.getProviderType());
    return oAuth2Template.fetchUserInfo(token.getAccessToken());
  }

  private OAuth2ProviderToken findTokenById(String oAuthId) {
    return this.providerTokenRepository
        .findById(oAuthId)
        .orElseThrow(() -> new IllegalArgumentException("OAuth2ProviderToken not found"));
  }
}
