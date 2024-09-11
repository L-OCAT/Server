package com.locat.api.domain.user.service;

public interface UserWithdrawalLogService {

  void withdrawal(final Long id, final String reason);
}
