package com.locat.api.domain.admin.service;

import com.locat.api.domain.admin.entity.AdminActivity;

public interface AdminAuditService {

  /**
   * 관리자 활동 감사 기록
   *
   * @param adminActivity 로그를 남길 관리자 활동 정보
   */
  void doRecord(AdminActivity adminActivity);
}
