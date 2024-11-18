package com.locat.api.domain.user.service;

import com.locat.api.domain.user.entity.User;

public interface UserSettingService {

  /**
   * 사용자의 기본 설정을 등록합니다.
   *
   * @param user 설정을 등록할 사용자
   */
  void registerDefaultSettings(final User user);
}
