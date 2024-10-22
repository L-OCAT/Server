package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.auth.template.OAuth2TemplateFactory;
import com.locat.api.domain.user.dto.UserInfoUpdateDto;
import com.locat.api.domain.user.entity.EndUser;
import com.locat.api.domain.user.service.EndUserService;
import com.locat.api.domain.user.service.UserValidationService;
import com.locat.api.domain.user.service.UserWithdrawalLogService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.DuplicatedException;
import com.locat.api.global.exception.NoSuchEntityException;
import com.locat.api.global.utils.HashingUtils;
import com.locat.api.global.utils.ValidationUtils;
import com.locat.api.infrastructure.repository.user.EndUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EndUserServiceImpl implements EndUserService {

  private final EndUserRepository endUserRepository;
  private final OAuth2TemplateFactory oAuth2TemplateFactory;
  private final UserValidationService userValidationService;
  private final UserWithdrawalLogService userWithdrawalLogService;

  @Override
  public EndUser save(EndUser user) {
    return this.endUserRepository.save(user);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<EndUser> findById(final Long id) {
    return this.endUserRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<EndUser> findByEmail(String email) {
    return this.endUserRepository.findByEmailHash(HashingUtils.hash(email.trim()));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<EndUser> findEndUserByOAuthId(String oAuthId) {
    return this.endUserRepository.findByOauthId(oAuthId);
  }

  @Override
  public Page<EndUser> findAll(Pageable pageable) {
    return this.endUserRepository.findAll(pageable);
  }

  @Override
  public EndUser update(Long id, UserInfoUpdateDto infoUpdateDto) {
    final EndUser user =
        this.findById(id)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
    this.validateRequestFields(user, infoUpdateDto);
    return user.update(infoUpdateDto.email(), infoUpdateDto.nickname());
  }

  @Override
  public void delete(Long id, String reason) {
    this.endUserRepository
        .findById(id)
        .ifPresentOrElse(
            user -> {
              this.processOAuthWithdrawal(user);
              this.userWithdrawalLogService.save(id, reason);
              user.delete();
            },
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER);
            });
  }

  private void processOAuthWithdrawal(EndUser user) {
    final String oauthId = user.getOauthId();
    this.oAuth2TemplateFactory.getById(oauthId).withdrawal(oauthId);
  }

  private void validateRequestFields(EndUser user, UserInfoUpdateDto infoUpdateDto) {
    ValidationUtils.throwIf(
        infoUpdateDto.email(),
        value -> user.getEmail().equals(value),
        () -> new DuplicatedException(ApiExceptionType.RESOURCE_IDENTICAL));
    this.userValidationService.validateEmail(infoUpdateDto.email());

    ValidationUtils.throwIf(
        infoUpdateDto.nickname(),
        value -> user.getNickname().equals(value),
        () -> new DuplicatedException(ApiExceptionType.RESOURCE_IDENTICAL));
    this.userValidationService.validateNickname(infoUpdateDto.nickname());
  }
}
