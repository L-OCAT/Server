package com.locat.api.domain.admin.service.impl;

import com.locat.api.domain.admin.service.AdminInternalService;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.enums.UserType;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
import com.locat.api.global.utils.HashingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminInternalServiceImpl implements AdminInternalService {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void resetPassword(String userEmail, String newPassword) {
    this.userService
        .findByEmail(HashingUtils.hash(userEmail))
        .filter(User::isPasswordExpired)
        .ifPresentOrElse(
            adminUser -> adminUser.resetPassword(this.passwordEncoder.encode(newPassword)),
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER);
            });
  }

  @Override
  public void updateUserType(Long userId, Integer level) {
    this.userService
        .findById(userId)
        .ifPresentOrElse(
            adminUser -> adminUser.promote(UserType.fromLevel(level)),
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER);
            });
  }
}
