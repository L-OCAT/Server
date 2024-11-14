package com.locat.api.infra.aws.exception;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;

/** 메일 기능 처리(전송, 삭제 등)에 실패했을 때 발생하는 예외 */
public class MailOperationException extends LocatApiException {

  public MailOperationException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType);
  }
}
