package com.locat.api.domain.user.exception;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;

public class UserEndpointException extends LocatApiException {
  public UserEndpointException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType);
  }
}
