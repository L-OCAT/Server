package com.locat.api.domain.admin.service;

public interface AdminInternalService {

  /**
   * 관리자 사용자의 비밀번호 초기화
   *
   * @param userEmail 비밀번호를 초기화할 사용자 이메일
   * @param newPassword 초기화할 비밀번호
   */
  void resetPassword(final String userEmail, final String newPassword);

  /**
   * 사용자의 권한 타입 변경
   *
   * @param userId 권한 타입을 변경할 사용자 ID
   * @param level 변경할 권한 타입 레벨
   * @see com.locat.api.domain.user.enums.UserType
   */
  void updateUserType(final Long userId, final Integer level);
}
