package com.locat.api.infra.persistence.setting;

import com.locat.api.domain.setting.AppSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingRepository extends JpaRepository<AppSetting, Long> {}
