package com.locat.api.unit.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import com.locat.api.domain.auth.dto.internal.KakaoUserInfo;
import com.locat.api.domain.auth.dto.internal.OAuth2UserInfo;
import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.auth.template.OAuth2Template;
import com.locat.api.domain.auth.template.OAuth2TemplateFactory;
import com.locat.api.domain.user.dto.internal.UserRegisterDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.domain.user.enums.UserInfoValidationType;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.domain.user.service.UserSettingService;
import com.locat.api.domain.user.service.UserTermsService;
import com.locat.api.domain.user.service.UserValidationService;
import com.locat.api.domain.user.service.impl.UserRegistrationServiceImpl;
import com.locat.api.global.exception.custom.DuplicatedException;
import com.locat.api.infra.aws.s3.LocatS3Client;
import com.locat.api.infra.redis.OAuth2ProviderTokenRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

class UserRegistrationServiceTest {

  private static final String MOCK_OAUTH_ID = "oAuth_1234";
  private static final UserRegisterDto USER_REGISTER_DTO =
      new UserRegisterDto("oAuth_1234", "LOCAT1555", true, true, true, true);

  @InjectMocks private UserRegistrationServiceImpl service;
  @Mock private UserService userService;
  @Mock private UserTermsService userTermsService;
  @Mock private UserSettingService userSettingService;
  @Mock private UserValidationService userValidationService;
  @Mock private OAuth2TemplateFactory oAuth2TemplateFactory;
  @Mock private OAuth2ProviderTokenRepository providerTokenRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private LocatS3Client s3Client;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    ReflectionTestUtils.setField(this.service, "tempPassword", "TempPassword!", String.class);
  }

  @Test
  @DisplayName("사용자 등록에 성공해야 한다.")
  void shouldRegisterUser() {
    // Given
    OAuth2Template mockTemplate = mock(OAuth2Template.class);
    OAuth2ProviderToken mockToken = mock(OAuth2ProviderToken.class);
    KakaoUserInfo mockUserInfo = mock(KakaoUserInfo.class);

    given(this.userValidationService.isExists(MOCK_OAUTH_ID, UserInfoValidationType.OAUTH_ID))
        .willReturn(false);
    willDoNothing().given(this.userValidationService).validateNickname("LOCAT1555");
    this.stubbingOAuths(mockTemplate, mockToken, mockUserInfo);

    // When
    User user = this.service.register(USER_REGISTER_DTO);

    // Then
    assertThat(user).isNotNull();
    verify(this.userService, times(1)).save(user);
    verify(this.userSettingService, times(1)).registerDefaultSettings(user);
    verify(this.userTermsService, times(1)).register(user, USER_REGISTER_DTO);
    verifyNoMoreInteractions(this.userService, this.userSettingService, this.userTermsService);
  }

  private void stubbingOAuths(
      OAuth2Template mockTemplate, OAuth2ProviderToken mockToken, OAuth2UserInfo mockUserInfo) {
    given(mockToken.getId()).willReturn(MOCK_OAUTH_ID);
    given(mockToken.getProviderType()).willReturn(OAuth2ProviderType.KAKAO);

    given(this.providerTokenRepository.findById(MOCK_OAUTH_ID)).willReturn(Optional.of(mockToken));
    given(this.oAuth2TemplateFactory.getByType(OAuth2ProviderType.KAKAO)).willReturn(mockTemplate);

    given(mockTemplate.fetchUserInfo(MOCK_OAUTH_ID)).willReturn(mockUserInfo);
    given(mockUserInfo.getEmail()).willReturn("mock-user@locat.kr");
    given(mockUserInfo.getProvider()).willReturn(OAuth2ProviderType.KAKAO);
  }

  @Test
  @DisplayName("사용자가 이미 존재한다면, 예외를 던져야 한다.")
  void testWhenUserAlreadyExists() {
    // Given
    given(this.userValidationService.isExists(MOCK_OAUTH_ID, UserInfoValidationType.OAUTH_ID))
        .willReturn(true);

    // When & Then
    assertThatThrownBy(() -> this.service.register(USER_REGISTER_DTO))
        .isExactlyInstanceOf(DuplicatedException.class);
    verifyNoInteractions(
        this.userService,
        this.userSettingService,
        this.userTermsService,
        this.passwordEncoder,
        this.providerTokenRepository,
        this.oAuth2TemplateFactory,
        this.s3Client); // 의도한 지점에서 중단되었는지 확인
  }

  @Test
  @DisplayName("OAuth 제공자의 토큰이 존재하지 않는 경우, 예외를 던져야 한다.")
  void testWhenOAuthProviderTokenIsNull() {
    // Given
    given(this.userValidationService.isExists(MOCK_OAUTH_ID, UserInfoValidationType.OAUTH_ID))
        .willReturn(false);
    given(this.providerTokenRepository.findById(MOCK_OAUTH_ID)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> this.service.register(USER_REGISTER_DTO))
        .isExactlyInstanceOf(IllegalArgumentException.class);
    verifyNoInteractions(
        this.userService,
        this.userSettingService,
        this.userTermsService,
        this.passwordEncoder,
        this.s3Client);
  }

  @Test
  @DisplayName("주어진 이미지 객체 목록 중 하나를 랜덤하게 반환해야 한다.")
  void testGetRandomProfileImage() {
    // Given
    List<String> availableProfileImages =
        List.of(
            "https://mock-bucket.amazonaws.com/users/profiles/1.jpg",
            "https://mock-bucket.amazonaws.com/users/profiles/2.jpg",
            "https://mock-bucket.amazonaws.com/users/profiles/3.jpg",
            "https://mock-bucket.amazonaws.com/users/profiles/4.jpg");
    given(this.s3Client.getListObjects(anyString())).willReturn(availableProfileImages);

    // When
    String profileImage = ReflectionTestUtils.invokeMethod(this.service, "getRandomProfileImage");

    // Then
    assertThat(availableProfileImages).contains(profileImage);
  }

  @Test
  @DisplayName("S3 버킷에 이미지가 없는 경우, null을 반환해야 한다.")
  void testGetRandomProfileImageWhenBucketIsEmpty() {
    // Given
    given(this.s3Client.getListObjects(anyString())).willReturn(List.of());

    // When
    String actual = ReflectionTestUtils.invokeMethod(this.service, "getRandomProfileImage");

    // Then
    assertThat(actual).isNull();
  }
}
