package com.locat.api.domain.user.service;

import com.locat.api.domain.user.entity.EndUser;

public interface UserSettingService {

  void registerDefaultSettings(final EndUser user);
}
