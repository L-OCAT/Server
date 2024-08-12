package com.locat.api.domain.auth.service.impl;

import com.locat.api.domain.auth.entity.VerificationCode;
import com.locat.api.domain.auth.service.AuthService;
import com.locat.api.global.auth.AuthenticationException;
import com.locat.api.global.auth.jwt.JwtProvider;
import com.locat.api.global.auth.jwt.LocatTokenDto;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.mail.MailService;
import com.locat.api.global.mail.MailTemplate;
import com.locat.api.global.utils.RandomCodeGenerator;
import com.locat.api.infrastructure.redis.VerificationCodeRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  public static final int VERIFICATION_CODE_LENGTH = 6;
  public static final Duration VERIFICATION_CODE_EXPIRATION = Duration.ofMinutes(5);

  private final MailService mailService;
  private final JwtProvider jwtProvider;
  private final VerificationCodeRepository verificationCodeRepository;

  @Override
  public LocatTokenDto renew(String accessToken, String refreshToken) {
    return this.jwtProvider.renew(accessToken, refreshToken);
  }

  @Override
  public void sendVerificationEmail(String email) {
    final String verificationCode = RandomCodeGenerator.generate(VERIFICATION_CODE_LENGTH);
    this.verificationCodeRepository.save(
        VerificationCode.of(email, verificationCode, VERIFICATION_CODE_EXPIRATION.toSeconds()));
    this.mailService.send(
        email,
        MailTemplate.MAIL_VERIFY_TITLE,
        MailTemplate.createMailVerifyMessage(verificationCode));
  }

  @Override
  public void verify(String email, String code) {
    if (this.isVerificationCodeInValid(email, code)) {
      throw new AuthenticationException(ApiExceptionType.INVALID_EMAIL_VERIFICATION_CODE);
    }
  }

  private boolean isVerificationCodeInValid(String email, String code) {
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
