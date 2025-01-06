package com.locat.api.global.exception.custom;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;
import lombok.Getter;

/**
 * 중복된 리소스가 존재할 때 발생하는 예외 <br>
 * <li>이미 존재하는 사용자 정보로 회원가입을 시도할 때 등
 */
@Getter
public class DuplicatedException extends LocatApiException {

  public DuplicatedException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType);
  }
}
