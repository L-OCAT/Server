package com.locat.api.domain.auth.service.impl;

import com.locat.api.domain.auth.service.AuthService;
import com.locat.api.global.auth.AuthenticationException;
import com.locat.api.global.auth.jwt.JwtProvider;
import com.locat.api.global.auth.jwt.LocatTokenDto;
import com.locat.api.global.constant.RedisConstant;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.mail.MailService;
import com.locat.api.global.mail.MailTemplate;
import com.locat.api.global.utils.RandomCodeGenerator;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  public static final int VERIFICATION_CODE_LENGTH = 6;
  public static final Duration VERIFICATION_CODE_EXPIRATION = Duration.ofMinutes(5);

  private final MailService mailService;
  private final JwtProvider jwtProvider;
  private final ValueOperations<String, String> verificationCodeCache;

  @Override
  public LocatTokenDto renew(String accessToken, String refreshToken) {
    return this.jwtProvider.renew(accessToken, refreshToken);
  }

  @Override
  public void sendVerificationEmail(String email) {
    final String verificationCode = RandomCodeGenerator.generate(VERIFICATION_CODE_LENGTH);
    this.saveVerificationCodeToCache(email, verificationCode);
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
    final String key = RedisConstant.EMAIL_VERIFICATION_CODE.concat(email);
    final String cachedCode = this.verificationCodeCache.get(key);
    return !code.equals(cachedCode);
  }

  private void saveVerificationCodeToCache(String email, String verificationCode) {
    final String key = RedisConstant.EMAIL_VERIFICATION_CODE.concat(email);
    this.verificationCodeCache.set(key, verificationCode, VERIFICATION_CODE_EXPIRATION);
  }
}
