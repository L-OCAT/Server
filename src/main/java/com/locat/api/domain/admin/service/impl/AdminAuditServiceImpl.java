package com.locat.api.domain.admin.service.impl;

import com.locat.api.domain.admin.entity.AdminActivity;
import com.locat.api.domain.admin.service.AdminAuditService;
import com.locat.api.global.event.AdminAuditEvent;
import com.locat.api.infra.persistence.admin.AdminActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminAuditServiceImpl implements AdminAuditService {

  private final AdminActivityRepository activityRepository;

  @Override
  public void doRecord(AdminActivity adminActivity) {
    this.activityRepository.save(adminActivity);
  }

  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(
      classes = AdminAuditEvent.class,
      phase = TransactionPhase.AFTER_COMPLETION,
      fallbackExecution = true)
  public void handleAdminAuditEvent(AdminAuditEvent event) {
    this.doRecord(AdminActivity.from(event));
  }
}
