package com.locat.api.global.exception.custom;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;

/**
 * Entity가 존재하지 않을 때 발생하는 예외 <br>
 * <li>주어진 ID, Key 등 정보로 Entity 조회에 실패했을 때 등
 *
 * @apiNote {@link ApiExceptionType}의 {@code NOT_FOUND_*}와 함께 사용하는 것을 권장합니다.
 */
public class NoSuchEntityException extends LocatApiException {

  public NoSuchEntityException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType);
  }
}
