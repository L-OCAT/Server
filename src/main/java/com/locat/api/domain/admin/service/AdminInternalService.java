package com.locat.api.domain.admin.service;

public interface AdminInternalService {

  void resetPassword(final Long userId, final String newPassword);

  void promote(final Long userId, final Integer level);
}
