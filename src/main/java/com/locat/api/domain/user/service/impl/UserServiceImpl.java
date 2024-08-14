package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.infrastructure.repository.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public User save(User user) {
    return this.userRepository.save(user);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByEmail(String email) {
    return this.userRepository.findByEmail(email);
  }

  @Override
  public Optional<User> findByOauthId(String oauthId) {
    return this.userRepository.findByOauthId(oauthId);
  }
}