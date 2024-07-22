package com.locat.api.global.file;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;

/** 파일 업로드/삭제 등 파일 작업에 실패했을 때 사용 */
public class FileOperationFailedException extends LocatApiException {

  public FileOperationFailedException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType);
  }
}
