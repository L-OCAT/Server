package com.locat.api.global.exception;

import org.springframework.security.core.AuthenticationException;

public class NoApiKeyException extends AuthenticationException {

  public NoApiKeyException(String message) {
    super(message);
  }
}
