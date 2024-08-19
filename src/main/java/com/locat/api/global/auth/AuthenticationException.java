package com.locat.api.global.auth;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;

public class AuthenticationException extends LocatApiException {

  public AuthenticationException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType);
  }
}
