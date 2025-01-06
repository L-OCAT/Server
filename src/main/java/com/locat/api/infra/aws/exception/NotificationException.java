package com.locat.api.infra.aws.exception;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;

/** Push Notification 처리 중 발생하는 예외 */
public class NotificationException extends LocatApiException {

  public NotificationException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType);
  }

  public NotificationException(ApiExceptionType apiExceptionType, Throwable cause) {
    super(apiExceptionType, cause);
  }
}
