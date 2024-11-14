package com.locat.api.domain.user.service.impl;

import static com.locat.api.domain.user.enums.UserInfoValidationType.EMAIL;
import static com.locat.api.domain.user.enums.UserInfoValidationType.NICKNAME;
import static com.locat.api.global.utils.ValidationUtils.*;

import com.locat.api.domain.user.enums.UserInfoValidationType;
import com.locat.api.domain.user.service.UserValidationService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.custom.DuplicatedException;
import com.locat.api.global.exception.custom.InvalidParameterException;
import com.locat.api.global.utils.HashingUtils;
import com.locat.api.global.utils.ValidationUtils;
import com.locat.api.infra.persistence.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public Boolean isExists(String value, UserInfoValidationType type) {
    return switch (type) {
      case OAUTH_ID -> this.userRepository.existsByOauthId(value);
      case EMAIL -> this.userRepository.existsByEmailHash(HashingUtils.hash(value));
      case NICKNAME -> this.userRepository.existsByNickname(value);
    };
  }

  @Override
  public void validateNickname(String nickname) {
    ValidationUtils.throwIfAny(
        nickname,
        List.of(
            FORBIDDEN_NICKNAME_PATTERN.asMatchPredicate(),
            value1 -> !NICKNAME_PATTERN.matcher(value1).matches(),
            value2 -> this.isExists(value2, NICKNAME)),
        List.of(
            () -> new InvalidParameterException("Forbidden nickname"),
            () -> new InvalidParameterException("Invalid nickname format"),
            () -> new DuplicatedException(ApiExceptionType.RESOURCE_ALREADY_EXISTS)));
  }

  @Override
  public void validateEmail(String email) {
    ValidationUtils.throwIfAny(
        email,
        List.of(
            value1 -> !EMAIL_PATTERN.matcher(value1).matches(),
            value2 -> this.isExists(value2, EMAIL)),
        List.of(
            () -> new InvalidParameterException("Invalid email format"),
            () -> new DuplicatedException(ApiExceptionType.RESOURCE_ALREADY_EXISTS)));
  }
}
