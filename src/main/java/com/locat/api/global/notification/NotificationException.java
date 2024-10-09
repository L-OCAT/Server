package com.locat.api.global.notification;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;

public class NotificationException extends LocatApiException {

  public NotificationException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType);
  }

  public NotificationException(ApiExceptionType apiExceptionType, Throwable cause) {
    super(apiExceptionType, cause);
  }
}
