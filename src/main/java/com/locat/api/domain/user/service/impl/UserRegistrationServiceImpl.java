package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.auth.dto.internal.OAuth2UserInfo;
import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.auth.template.OAuth2Template;
import com.locat.api.domain.auth.template.OAuth2TemplateFactory;
import com.locat.api.domain.user.dto.internal.UserRegisterDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.enums.UserInfoValidationType;
import com.locat.api.domain.user.service.*;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.custom.DuplicatedException;
import com.locat.api.global.utils.RandomGenerator;
import com.locat.api.global.utils.ValidationUtils;
import com.locat.api.infra.aws.s3.LocatS3Client;
import com.locat.api.infra.redis.OAuth2ProviderTokenRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {

  private static final String PROFILE_IMAGES_DIRECTORY = "users/profiles";

  private final UserService userService;
  private final UserTermsService userTermsService;
  private final UserSettingService userSettingService;
  private final UserValidationService userValidationService;
  private final OAuth2TemplateFactory oAuth2TemplateFactory;
  private final OAuth2ProviderTokenRepository providerTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final LocatS3Client s3Client;

  @Value("${service.admin.temp-password}")
  private String tempPassword;

  @Override
  public User register(UserRegisterDto userRegisterDto) {
    this.assertUserNotExists(userRegisterDto.oAuthId());
    this.userValidationService.validateNickname(userRegisterDto.nickname());

    OAuth2ProviderToken token = this.findTokenById(userRegisterDto.oAuthId());
    OAuth2UserInfo userInfo = this.fetchUserInfo(token);
    String profileImage = this.getRandomProfileImage();
    final User user =
        User.of(
            profileImage,
            userRegisterDto.nickname(),
            this.passwordEncoder.encode(this.tempPassword),
            userInfo);

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

  private OAuth2UserInfo fetchUserInfo(OAuth2ProviderToken token) {
    OAuth2Template oAuth2Template = this.oAuth2TemplateFactory.getByType(token.getProviderType());
    return oAuth2Template.fetchUserInfo(token.getId());
  }

  private OAuth2ProviderToken findTokenById(String oAuthId) {
    return this.providerTokenRepository
        .findById(oAuthId)
        .orElseThrow(() -> new IllegalArgumentException("OAuth2ProviderToken not found"));
  }

  private String getRandomProfileImage() {
    List<String> availableProfileImages = this.s3Client.getListObjects(PROFILE_IMAGES_DIRECTORY);
    if (availableProfileImages.isEmpty()) {
      return null;
    }
    final int imageIndex = RandomGenerator.nextInt(availableProfileImages.size());
    return availableProfileImages.get(imageIndex);
  }
}
