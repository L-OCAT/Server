package com.locat.api.global.exception;

import lombok.Getter;

@Getter
public class DuplicatedException extends LocatApiException {

  public DuplicatedException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType);
  }
}
