package com.locat.api.domain.auth.service.impl;

import com.locat.api.domain.auth.entity.VerificationCode;
import com.locat.api.domain.auth.exception.EmailAlreadySentException;
import com.locat.api.domain.auth.service.AuthService;
import com.locat.api.global.auth.AuthenticationException;
import com.locat.api.global.auth.jwt.JwtProvider;
import com.locat.api.global.auth.jwt.LocatTokenDto;
import com.locat.api.global.event.UserAuthenticatedEvent;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.mail.MailService;
import com.locat.api.global.mail.MailTemplate;
import com.locat.api.global.utils.RandomCodeGenerator;
import com.locat.api.infrastructure.redis.VerificationCodeRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  public static final int VERIFICATION_CODE_LENGTH = 6;
  public static final Duration VERIFICATION_CODE_EXPIRATION = Duration.ofMinutes(5);

  private final MailService mailService;
  private final JwtProvider jwtProvider;
  private final VerificationCodeRepository verificationCodeRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public LocatTokenDto renew(final String accessToken, final String refreshToken) {
    return this.jwtProvider.renew(accessToken, refreshToken);
  }

  @Override
  public void sendVerificationEmail(final String email) {
    this.checkIfEmailIsAlreadySent(email);
    final String verificationCode = RandomCodeGenerator.generate(VERIFICATION_CODE_LENGTH);
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
    this.eventPublisher.publishEvent(UserAuthenticatedEvent.of(this, email));
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
