package com.locat.api.domain.admin.service;

import com.locat.api.domain.admin.entity.AdminActivity;

public interface AdminAuditService {

  void doRecord(AdminActivity adminActivity);
}
