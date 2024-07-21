package com.locat.api.global.mail;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;

/** 메일 전송에 실패했을 때 사용 */
public class MailOperationFailedException extends LocatApiException {

  public MailOperationFailedException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType);
  }
}
