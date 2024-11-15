package com.locat.api.domain.user.service;

import com.locat.api.domain.user.dto.internal.UserRegisterDto;
import com.locat.api.domain.user.entity.User;

public interface UserRegistrationService {
  User register(final UserRegisterDto userRegisterDto);
}
