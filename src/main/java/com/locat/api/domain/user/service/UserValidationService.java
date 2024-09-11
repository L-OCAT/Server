package com.locat.api.domain.user.service;

public interface UserValidationService {

  /**
   * 이메일 중복 여부 확인
   *
   * @param email 중복인지 확인할 이메일
   * @return 중복 여부
   */
  Boolean isEmailExists(final String email);

  /**
   * 닉네임 중복 여부 확인
   *
   * @param nickname 중복인지 확인할 닉네임
   * @return 중복 여부
   */
  Boolean isNicknameExists(final String nickname);
}
