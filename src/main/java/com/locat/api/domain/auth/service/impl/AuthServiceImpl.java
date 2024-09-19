package com.locat.api.domain.auth.service.impl;

import com.locat.api.domain.auth.entity.VerificationCode;
import com.locat.api.domain.auth.exception.EmailAlreadySentException;
import com.locat.api.domain.auth.service.AuthService;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  public static final int VERIFICATION_CODE_LENGTH = 6;
  public static final Duration VERIFICATION_CODE_EXPIRATION = Duration.ofMinutes(5);

  private final UserService userService;
  private final MailService mailService;
  private final JwtProvider jwtProvider;
  private final VerificationCodeRepository verificationCodeRepository;

  @Override
  public LocatTokenDto authenticate(String oAuthId) { // 보안 체크
    return this.userService
        .findByOAuthId(oAuthId)
        .map(User::getId)
        .map(this.jwtProvider::create)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
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
