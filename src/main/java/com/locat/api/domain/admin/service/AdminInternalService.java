package com.locat.api.domain.admin.service;

public interface AdminInternalService {

  /**
   * 관리자 사용자의 초기 비밀번호 초기화
   *
   * @param userId 비밀번호를 초기화할 사용자 ID
   * @param newPassword 초기화할 비밀번호
   */
  void resetPassword(final Long userId, final String newPassword);

  /**
   * 사용자의 유저 타입 변경
   *
   * @param userId 유저 타입을 변경할 사용자 ID
   * @param level 변경할 유저 타입 레벨
   */
  void updateUserType(final Long userId, final Integer level);
}
