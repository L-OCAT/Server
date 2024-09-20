package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.user.entity.UserWithdrawalLog;
import com.locat.api.domain.user.service.UserWithdrawalLogService;
import com.locat.api.infrastructure.repository.user.UserWithdrawalLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWithdrawalLogServiceImpl implements UserWithdrawalLogService {

  private final UserWithdrawalLogRepository withdrawalLogRepository;

  @Override
  public void save(Long id, String reason) {
    this.withdrawalLogRepository.save(UserWithdrawalLog.of(id, reason));
  }
}
