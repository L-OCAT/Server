package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.user.entity.UserOAuth;
import com.locat.api.domain.user.service.UserOAuthService;
import com.locat.api.infrastructure.repository.user.UserOAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserOAuthServiceImpl implements UserOAuthService {

  private final UserOAuthRepository userOAuthRepository;

  @Override
  public UserOAuth save(UserOAuth userOAuth) {
    return this.userOAuthRepository.save(userOAuth);
  }
}
