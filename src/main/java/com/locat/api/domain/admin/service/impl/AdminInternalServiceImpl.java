package com.locat.api.domain.admin.service.impl;

import com.locat.api.domain.admin.service.AdminInternalService;
import com.locat.api.domain.user.entity.AdminUser;
import com.locat.api.domain.user.service.AdminUserService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminInternalServiceImpl implements AdminInternalService {

  private final AdminUserService adminUserService;
  private final PasswordEncoder passwordEncoder;

  @Value("${service.admin.temp-password}")
  private String tempPassword;

  @Override
  public void resetPassword(Long userId, String newPassword) {
    this.adminUserService
        .findById(userId)
        .filter(AdminUser::isPasswordExpired)
        .ifPresentOrElse(
            adminUser -> adminUser.resetPassword(this.passwordEncoder.encode(newPassword)),
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER);
            });
  }

  @Override
  public void updateUserType(Long userId, Integer level) {
    this.adminUserService
        .findById(userId)
        .ifPresentOrElse(
            adminUser -> adminUser.promote(level, this.passwordEncoder.encode(this.tempPassword)),
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER);
            });
  }
}
