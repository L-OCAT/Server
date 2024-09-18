package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.auth.template.OAuth2Template;
import com.locat.api.domain.auth.template.OAuth2TemplateFactory;
import com.locat.api.domain.user.dto.OAuth2UserInfoDto;
import com.locat.api.domain.user.dto.UserRegisterDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserRegistrationService;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.domain.user.service.UserSettingService;
import com.locat.api.domain.user.service.UserTermsService;
import com.locat.api.infrastructure.redis.OAuth2ProviderTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {

  private final UserService userService;
  private final UserTermsService userTermsService;
  private final UserSettingService userSettingService;
  private final OAuth2TemplateFactory oAuth2TemplateFactory;
  private final OAuth2ProviderTokenRepository providerTokenRepository;

  @Override
  public User register(UserRegisterDto userRegisterDto) {
    OAuth2ProviderToken token = this.findTokenById(userRegisterDto.oAuthId());
    OAuth2UserInfoDto userInfo = this.fetchUserInfo(token);
    final User user = User.fromOAuth(userRegisterDto.nickname(), userInfo);

    this.userService.save(user);
    //    this.userSettingService.registerDefaultSettings(user); // TODO: Uncomment
    this.userTermsService.register(user, userRegisterDto);
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
