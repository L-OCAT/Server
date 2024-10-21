package com.locat.api.domain.admin.service;

import com.locat.api.domain.admin.dto.AdminUserStatDto;

public interface AdminStatisticService {

  AdminUserStatDto getEndUserStat(Long id);
}
