package com.locat.api.global.exception;

import lombok.Getter;

@Getter
public class InternalProcessingException extends LocatApiException {

  public InternalProcessingException(final String message) {
    super(ApiExceptionType.INTERNAL_SERVER_ERROR);
    log.warn(message);
  }
}
