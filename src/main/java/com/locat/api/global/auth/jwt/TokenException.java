package com.locat.api.global.auth.jwt;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;

/** 토큰 관련 예외 처리 클래스 */
public class TokenException extends LocatApiException {

  public TokenException() {
    super(ApiExceptionType.INVALID_TOKEN);
  }

  public TokenException(Throwable cause) {
    super(ApiExceptionType.INVALID_TOKEN, cause);
  }
}
