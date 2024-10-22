package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.user.entity.AdminUser;
import com.locat.api.domain.user.service.AdminUserService;
import com.locat.api.global.utils.HashingUtils;
import com.locat.api.infrastructure.repository.user.AdminUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

  private final AdminUserRepository adminUserRepository;

  @Override
  public Optional<AdminUser> findById(Long id) {
    return adminUserRepository.findById(id);
  }

  @Override
  public Optional<AdminUser> findByEmail(String email) {
    return this.adminUserRepository.findByEmailHash(HashingUtils.hash(email));
  }
}
