package com.locat.api.global.exception.custom;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;
import lombok.Getter;

/** 요청 파라미터가 null이거나, 잘못된 형식일 경우 던지는 예외 */
@Getter
public class InvalidParameterException extends LocatApiException {

  public InvalidParameterException(final String message) {
    super(ApiExceptionType.INVALID_PARAMETER);
    log.debug(message);
  }
}
