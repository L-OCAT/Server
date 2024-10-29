package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.association.UserSetting;
import com.locat.api.domain.user.service.UserSettingService;
import com.locat.api.infrastructure.repository.setting.AppSettingRepository;
import com.locat.api.infrastructure.repository.user.UserSettingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSettingServiceImpl implements UserSettingService {

  private final AppSettingRepository appSettingRepository;
  private final UserSettingRepository userSettingRepository;

  @Override
  public void registerDefaultSettings(User user) {
    List<UserSetting> userSettings =
        this.appSettingRepository.findAll().stream()
            .map(setting -> UserSetting.ofDefault(user, setting))
            .toList();
    this.userSettingRepository.saveAll(userSettings);
  }
}
