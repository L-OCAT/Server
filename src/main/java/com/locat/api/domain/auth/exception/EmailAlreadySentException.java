package com.locat.api.domain.auth.exception;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;
import lombok.Getter;

/** 이미 이메일이 발송되었을 때 발생하는 예외 */
@Getter
public class EmailAlreadySentException extends LocatApiException {

  /**
   * 다음 이메일 발송까지 대기해야 하는 시간 (초 단위) <br>
   * 이전에 발송된 인증 코드가 만료되기까지 남은 시간을 나타냅니다.
   */
  private final long retryAfter;

  public EmailAlreadySentException(final long retryAfter) {
    super(ApiExceptionType.VERIFICATION_EMAIL_ALREADY_SENT);
    this.retryAfter = retryAfter;
  }
}
