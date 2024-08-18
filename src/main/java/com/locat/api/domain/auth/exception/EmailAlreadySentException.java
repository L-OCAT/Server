package com.locat.api.domain.auth.exception;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;
import lombok.Getter;

@Getter
public class EmailAlreadySentException extends LocatApiException {

  private final long retryAfter;

  public EmailAlreadySentException(final long retryAfter) {
    super(ApiExceptionType.VERIFICATION_EMAIL_ALREADY_SENT);
    this.retryAfter = retryAfter;
  }
}
