package com.locat.api.infrastructure.repository.user;

import com.locat.api.domain.user.entity.association.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {}
