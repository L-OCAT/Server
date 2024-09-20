package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.auth.template.OAuth2Template;
import com.locat.api.domain.auth.template.OAuth2TemplateFactory;
import com.locat.api.domain.user.dto.OAuth2UserInfoDto;
import com.locat.api.domain.user.dto.UserRegisterDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.enums.UserInfoValidationType;
import com.locat.api.domain.user.service.*;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.DuplicatedException;
import com.locat.api.global.utils.ValidationUtils;
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
  private final UserValidationService userValidationService;
  private final OAuth2TemplateFactory oAuth2TemplateFactory;
  private final OAuth2ProviderTokenRepository providerTokenRepository;

  @Override
  public User register(UserRegisterDto userRegisterDto) {
    this.assertUserNotExists(userRegisterDto.oAuthId());
    this.userValidationService.validateNickname(userRegisterDto.nickname());

    OAuth2ProviderToken token = this.findTokenById(userRegisterDto.oAuthId());
    OAuth2UserInfoDto userInfo = this.fetchUserInfo(token);
    final User user = User.of(userRegisterDto.nickname(), userInfo);

    this.userService.save(user);
    this.userSettingService.registerDefaultSettings(user);
    this.userTermsService.register(user, userRegisterDto);
    return user;
  }

  private void assertUserNotExists(String oAuthId) {
    ValidationUtils.throwIf(
        oAuthId,
        value -> this.userValidationService.isExists(value, UserInfoValidationType.OAUTH_ID),
        () -> new DuplicatedException(ApiExceptionType.RESOURCE_ALREADY_EXISTS));
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
