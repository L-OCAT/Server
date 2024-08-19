package com.locat.api.domain.user.service;

import com.locat.api.domain.user.entity.User;

public interface UserRegistrationService {
  User registerByOAuth(final String oAuthId);
}
