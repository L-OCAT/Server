package com.locat.api.infra.aws.exception;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;

/** 파일 업로드/삭제 등 파일 작업에 실패했을 때 발생하는 예외 */
public class FileOperationException extends LocatApiException {

  public FileOperationException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType);
  }
}
