package com.locat.api.domain.user.service;

public interface UserWithdrawalLogService {

  void save(final Long id, final String reason);
}
