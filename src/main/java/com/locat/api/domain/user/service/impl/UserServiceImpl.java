package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.auth.template.OAuth2TemplateFactory;
import com.locat.api.domain.user.dto.UserInfoUpdateDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.domain.user.service.UserWithdrawalLogService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
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
  private final OAuth2TemplateFactory oAuth2TemplateFactory;
  private final UserWithdrawalLogService userWithdrawalLogService;

  @Override
  public User save(User user) {
    return this.userRepository.save(user);
  }

  @Override
  public User update(Long id, UserInfoUpdateDto infoUpdateDto) {
    final User user = this.findById(id);
    final String email = infoUpdateDto.email();
    return user.update(email, infoUpdateDto.nickname());
  }

  @Override
  @Transactional(readOnly = true)
  public User findById(final Long id) {
    return this.userRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
  }

  @Override
  public Optional<User> findByOAuthId(String oAuthId) {
    return this.userRepository.findByOauthId(oAuthId);
  }

  @Override
  public void delete(Long id, String reason) {
    this.userRepository
        .findById(id)
        .ifPresentOrElse(
            user -> {
              this.processOAuthWithdrawal(user);
              this.userWithdrawalLogService.withdrawal(id, reason);
              user.delete();
            },
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER);
            });
  }

  private void processOAuthWithdrawal(User user) {
    final String oauthId = user.getOauthId();
    this.oAuth2TemplateFactory.getById(oauthId).withdrawal(oauthId);
  }
}
