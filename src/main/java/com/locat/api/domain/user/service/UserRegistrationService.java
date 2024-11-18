package com.locat.api.domain.user.service;

import com.locat.api.domain.user.dto.internal.UserRegisterDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.global.exception.custom.DuplicatedException;
import com.locat.api.global.exception.custom.InvalidParameterException;

public interface UserRegistrationService {

  /**
   * 사용자를 등록합니다.
   *
   * @param userRegisterDto 사용자 등록 정보
   * @return 등록된 사용자
   * @throws IllegalArgumentException 사용자의 OAuth 인증 정보를 찾을 수 없는 경우
   * @throws InvalidParameterException 사용자 등록 정보가 유효하지 않은 경우
   * @throws DuplicatedException 사용자 등록 요청 정보가 고유 제약 조건을 위반하는 경우
   */
  User register(final UserRegisterDto userRegisterDto);
}
