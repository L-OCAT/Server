package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.user.service.UserValidationService;
import com.locat.api.global.utils.HashingUtils;
import com.locat.api.infrastructure.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

  private final UserRepository userRepository;

  @Override
  public Boolean isEmailExists(String email) {
    return this.userRepository.existsByEmailHash(HashingUtils.hash(email));
  }

  @Override
  public Boolean isNicknameExists(String nickname) {
    return this.userRepository.existsByNickname(nickname);
  }
}
