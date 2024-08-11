package com.locat.api.global.exception;

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

  protected LocatApiException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType.getMessage());
    this.httpStatus = HttpStatus.resolve(apiExceptionType.getStatusCode());
    this.message = apiExceptionType.getMessage();
    this.code = apiExceptionType.getCode();
  }
}
