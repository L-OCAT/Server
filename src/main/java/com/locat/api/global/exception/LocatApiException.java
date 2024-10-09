package com.locat.api.global.exception;

import jakarta.annotation.Nullable;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

@Getter
public abstract class LocatApiException extends RuntimeException {

  protected static final Logger log = LoggerFactory.getLogger(LocatApiException.class);

  private final HttpStatus httpStatus;
  private final String message;
  private final Integer code;
  @Nullable private final Throwable cause;

  protected LocatApiException(ApiExceptionType apiExceptionType) {
    this(apiExceptionType, null);
  }

  protected LocatApiException(ApiExceptionType apiExceptionType, Throwable cause) {
    super(apiExceptionType.getMessage(), cause);
    this.httpStatus = HttpStatus.resolve(apiExceptionType.getStatusCode());
    this.message = apiExceptionType.getMessage();
    this.code = apiExceptionType.getCode();
    this.cause = cause;
  }
}
