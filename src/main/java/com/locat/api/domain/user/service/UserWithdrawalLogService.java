package com.locat.api.domain.user.service;

public interface UserWithdrawalLogService {

  /**
   * 회원 탈퇴 로그를 저장합니다.
   *
   * @param id 회원 ID
   * @param reason 탈퇴 사유
   */
  void save(final Long id, final String reason);
}
