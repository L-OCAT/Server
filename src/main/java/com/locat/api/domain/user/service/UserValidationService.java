package com.locat.api.domain.user.service;

import com.locat.api.domain.user.enums.UserInfoValidationType;
import com.locat.api.global.exception.DuplicatedException;
import com.locat.api.global.exception.InvalidParameterException;

public interface UserValidationService {

  /**
   * 주어진 값의 중복 여부를 확인합니다.
   *
   * @param value 확인할 값
   * @param type 값의 타입
   * @return 중복 여부
   * @see UserInfoValidationType
   */
  Boolean isExists(final String value, UserInfoValidationType type);

  /**
   * 사용자의 닉네임의 유효성을 검증합니다. <br>
   * <li>닉네임 규칙(2~12자, 한글 / 영어 / 숫자)에 부합하는지
   * <li>닉네임이 이미 사용 중인지
   * <li>사용이 제한되는 단어가 포함되어 있는지
   *
   * @param nickname 유효한지 검증할 닉네임
   * @throws InvalidParameterException 닉네임이 규칙에 맞지 않는 경우
   * @throws DuplicatedException 닉네임이 이미 사용 중인 경우
   */
  void validateNickname(final String nickname);

  /**
   * 사용자의 이메일의 유효성을 검증합니다. <br>
   * <li>유효한 이메일 포맷인지
   * <li>이메일이 이미 사용 중인지
   *
   * @param email 유효한지 검증할 이메일
   * @throws InvalidParameterException 이메일이 규칙에 맞지 않는 경우
   * @throws DuplicatedException 이메일이 이미 사용 중인 경우
   */
  void validateEmail(final String email);
}
