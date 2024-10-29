package com.locat.api.domain.user.service;

import com.locat.api.domain.user.entity.User;

public interface UserSettingService {

  void registerDefaultSettings(final User user);
}
