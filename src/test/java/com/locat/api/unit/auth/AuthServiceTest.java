package com.locat.api.unit.auth;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

import com.locat.api.domain.auth.dto.internal.AdminLoginDto;
import com.locat.api.domain.auth.dto.response.AdminLoginResponse;
import com.locat.api.domain.auth.entity.VerificationCode;
import com.locat.api.domain.auth.exception.EmailAlreadySentException;
import com.locat.api.domain.auth.service.OAuth2Service;
import com.locat.api.domain.auth.service.impl.AuthServiceImpl;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.enums.StatusType;
import com.locat.api.domain.user.enums.UserType;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.security.exception.AuthenticationException;
import com.locat.api.global.security.jwt.JwtProvider;
import com.locat.api.global.security.jwt.dto.LocatTokenDto;
import com.locat.api.global.utils.RandomGenerator;
import com.locat.api.infra.aws.ses.LocatSesClient;
import com.locat.api.infra.aws.ses.impl.MailTemplate;
import com.locat.api.infra.redis.VerificationCodeRepository;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

final class AuthServiceTest {

  @InjectMocks private AuthServiceImpl service;
  @Mock private UserService userService;
  @Mock private LocatSesClient sesClient;
  @Mock private JwtProvider jwtProvider;
  @Mock private OAuth2Service oAuth2Service;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private VerificationCodeRepository verificationCodeRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("OAuth ID 인증 성공 시 토큰을 발급해야 한다.")
  void authenticateWithOAuthId() {
    // Given
    String oAuthId = "valid-oauth-id";
    User user = mock(User.class);
    LocatTokenDto expectedToken = mock(LocatTokenDto.class);

    given(this.oAuth2Service.isAuthenticated(oAuthId)).willReturn(true);
    given(this.userService.findByOAuthId(oAuthId)).willReturn(Optional.of(user));
    given(this.jwtProvider.create(user)).willReturn(expectedToken);
    doNothing().when(user).assertActivated();

    // When
    LocatTokenDto result = this.service.authenticate(oAuthId);

    // Then
    assertThat(result).isEqualTo(expectedToken);
    then(user).should().assertActivated();
  }

  @Test
  @DisplayName("OAuth ID 인증 실패 시 예외를 던져야 한다.")
  void authenticateWithInvalidOAuthId() {
    // Given
    String oAuthId = "invalid-oauth-id";
    given(oAuth2Service.isAuthenticated(oAuthId)).willReturn(false);

    // When & Then
    assertThatThrownBy(() -> this.service.authenticate(oAuthId))
        .isExactlyInstanceOf(AuthenticationException.class)
        .hasMessageContaining(ApiExceptionType.UNAUTHORIZED.getMessage());
  }

  @Test
  @DisplayName("관리자 로그인이 성공하면 응답 정보를 반환해야 한다.")
  void authenticateAdminSuccessfully() {
    // Given
    String email = "admin@test.com";
    String password = "password";
    String deviceId = "device123";
    AdminLoginDto loginDto = new AdminLoginDto(deviceId, email, password);

    User adminUser =
        User.builder()
            .id(1L)
            .email(email)
            .password("encoded-password")
            .userType(UserType.SUPER_ADMIN)
            .statusType(StatusType.ACTIVE)
            .adminDeviceIds(Collections.emptyList())
            .build();
    LocatTokenDto token = mock(LocatTokenDto.class);

    given(this.userService.findByEmail(email)).willReturn(Optional.of(adminUser));
    given(this.passwordEncoder.matches(password, adminUser.getPassword())).willReturn(true);
    given(this.jwtProvider.create(adminUser)).willReturn(token);

    // When
    AdminLoginResponse response = this.service.authenticate(loginDto);

    // Then
    assertThat(response.needMfa()).isTrue();
    assertThat(response.token()).isEqualTo(token);
  }

  @Test
  @DisplayName("관리자 로그인 실패 시 예외를 던져야 한다.")
  void authenticateAdminWithInvalidCredentials() {
    // Given
    String email = "admin@test.com";
    String password = "wrong-password";
    AdminLoginDto loginDto = new AdminLoginDto(email, password, "device123");

    User adminUser = mock(User.class);

    given(this.userService.findByEmail(email)).willReturn(Optional.of(adminUser));
    given(adminUser.isAdmin()).willReturn(true);
    given(this.passwordEncoder.matches(password, adminUser.getPassword())).willReturn(false);

    // When & Then
    assertThatThrownBy(() -> this.service.authenticate(loginDto))
        .isExactlyInstanceOf(AuthenticationException.class)
        .hasMessageContaining(ApiExceptionType.UNAUTHORIZED.getMessage());
  }

  @Test
  @DisplayName("토큰 갱신에 성공하면 새 토큰을 반환해야 한다.")
  void renewToken() {
    // Given
    String accessToken = "old-access-token";
    String refreshToken = "valid-refresh-token";
    LocatTokenDto newToken = mock(LocatTokenDto.class);

    given(this.jwtProvider.renew(accessToken, refreshToken)).willReturn(newToken);

    // When
    LocatTokenDto result = this.service.renew(accessToken, refreshToken);

    // Then
    assertThat(result).isEqualTo(newToken);
  }

  @Test
  @DisplayName("인증 이메일 전송이 성공하면 SESClient가 호출되어야 한다.")
  void sendVerificationEmail() {
    // Given
    String email = "user@test.com";
    String verificationCode = "123456";

    try (MockedStatic<RandomGenerator> randomGenerator = mockStatic(RandomGenerator.class)) {
      randomGenerator
          .when(() -> RandomGenerator.nextCode(AuthServiceImpl.VERIFICATION_CODE_LENGTH))
          .thenReturn(verificationCode);

      given(this.verificationCodeRepository.existsById(email)).willReturn(false);

      // When & Then
      assertThatCode(() -> this.service.sendVerificationEmail(email)).doesNotThrowAnyException();
      verify(this.verificationCodeRepository).save(any(VerificationCode.class));
      verify(this.sesClient).send(eq(email), eq(MailTemplate.MAIL_VERIFY_TITLE), anyString());
    }
  }

  @Test
  @DisplayName("이미 인증 코드가 전송된 이메일에 대해 예외를 던져야 한다.")
  void sendVerificationEmailWhenAlreadySent() {
    // Given
    String email = "user@test.com";
    given(this.verificationCodeRepository.existsById(email)).willReturn(true);

    // When & Then
    assertThatThrownBy(() -> this.service.sendVerificationEmail(email))
        .isExactlyInstanceOf(EmailAlreadySentException.class);
  }

  @Test
  @DisplayName("인증 코드가 올바르면 사용자 인증을 완료해야 한다.")
  void verifyEmailWithValidCode() {
    // Given
    String email = "user@test.com";
    String code = "123456";

    VerificationCode verificationCode = mock(VerificationCode.class);

    given(this.verificationCodeRepository.findById(email))
        .willReturn(Optional.of(verificationCode));
    given(verificationCode.getCode()).willReturn(code);

    // When & Then
    assertThatCode(() -> this.service.verify(email, code)).doesNotThrowAnyException();
    then(this.verificationCodeRepository).should().deleteById(email);
  }

  @Test
  @DisplayName("인증 코드가 잘못되었으면 예외를 던져야 한다.")
  void verifyEmailWithInvalidCode() {
    // Given
    String email = "user@test.com";
    String code = "wrong-code";

    VerificationCode verificationCode = mock(VerificationCode.class);

    given(this.verificationCodeRepository.findById(email))
        .willReturn(Optional.of(verificationCode));
    given(verificationCode.getCode()).willReturn("correct-code");

    // When & Then
    assertThatThrownBy(() -> this.service.verify(email, code))
        .isExactlyInstanceOf(AuthenticationException.class)
        .hasMessageContaining(ApiExceptionType.INVALID_EMAIL_VERIFICATION_CODE.getMessage());
  }
}
