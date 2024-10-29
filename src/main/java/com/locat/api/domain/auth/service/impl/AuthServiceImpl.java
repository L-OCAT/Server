package com.locat.api.domain.auth.service.impl;

import com.locat.api.domain.auth.dto.AdminLoginDto;
import com.locat.api.domain.auth.dto.response.AdminLoginResponse;
import com.locat.api.domain.auth.entity.VerificationCode;
import com.locat.api.domain.auth.exception.EmailAlreadySentException;
import com.locat.api.domain.auth.service.AuthService;
import com.locat.api.domain.auth.service.OAuth2Service;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.auth.AuthenticationException;
import com.locat.api.global.auth.jwt.JwtProvider;
import com.locat.api.global.auth.jwt.LocatTokenDto;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
import com.locat.api.global.mail.MailService;
import com.locat.api.global.mail.MailTemplate;
import com.locat.api.global.utils.RandomGenerator;
import com.locat.api.infrastructure.redis.VerificationCodeRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  public static final int VERIFICATION_CODE_LENGTH = 6;
  public static final Duration VERIFICATION_CODE_EXPIRATION = Duration.ofMinutes(5);

  private final UserService userService;
  private final MailService mailService;
  private final JwtProvider jwtProvider;
  private final OAuth2Service oAuth2Service;
  private final PasswordEncoder passwordEncoder;
  private final VerificationCodeRepository verificationCodeRepository;

  @Override
  @Transactional(readOnly = true)
  public LocatTokenDto authenticate(String oAuthId) {
    this.validateAuthentication(oAuthId);
    return this.userService
        .findEndUserByOAuthId(oAuthId)
        .map(this::issueTokenIfActivated)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
  }

  @Override
  @Transactional(readOnly = true)
  public AdminLoginResponse authenticate(AdminLoginDto loginDto) {
    return this.userService
        .findByEmail(loginDto.userId())
        .filter(user -> this.passwordEncoder.matches(loginDto.password(), user.getPassword()))
        .map(
            user -> {
              final boolean isDeviceTrusted = !user.isTrustedDevice(loginDto.deviceId());
              final LocatTokenDto token = this.issueTokenIfActivated(user);
              return AdminLoginResponse.of(user.isPasswordExpired(), isDeviceTrusted, token);
            })
        .orElseThrow(() -> new AuthenticationException(ApiExceptionType.UNAUTHORIZED));
  }

  private LocatTokenDto issueTokenIfActivated(User user) {
    user.assertActivated();
    return this.jwtProvider.create(user);
  }

  @Override
  public LocatTokenDto renew(final String accessToken, final String refreshToken) {
    return this.jwtProvider.renew(accessToken, refreshToken);
  }

  @Override
  public void sendVerificationEmail(final String email) {
    this.checkIfEmailIsAlreadySent(email);
    final String verificationCode = RandomGenerator.generateRandomCode(VERIFICATION_CODE_LENGTH);
    this.verificationCodeRepository.save(
        VerificationCode.of(email, verificationCode, VERIFICATION_CODE_EXPIRATION.toSeconds()));
    this.mailService.send(
        email,
        MailTemplate.MAIL_VERIFY_TITLE,
        MailTemplate.createMailVerifyMessage(verificationCode));
  }

  @Override
  public void verify(final String email, final String code) {
    if (this.isVerificationCodeInvalid(email, code)) {
      throw new AuthenticationException(ApiExceptionType.INVALID_EMAIL_VERIFICATION_CODE);
    }
    this.finalizeUserAuthentication(email);
  }

  private void validateAuthentication(String oAuthId) {
    if (Boolean.FALSE.equals(this.oAuth2Service.isAuthenticated(oAuthId))) {
      throw new AuthenticationException(ApiExceptionType.UNAUTHORIZED);
    }
  }

  private void finalizeUserAuthentication(final String email) {
    this.verificationCodeRepository.deleteById(email);
  }

  private void checkIfEmailIsAlreadySent(String email) {
    if (this.verificationCodeRepository.existsById(email)) {
      throw new EmailAlreadySentException(VERIFICATION_CODE_EXPIRATION.toSeconds());
    }
  }

  private boolean isVerificationCodeInvalid(String email, String code) {
    final String cachedCode =
        this.verificationCodeRepository
            .findById(email)
            .map(VerificationCode::getCode)
            .orElseThrow(
                () ->
                    new AuthenticationException(ApiExceptionType.INVALID_EMAIL_VERIFICATION_CODE));
    return !code.equals(cachedCode);
  }
}
