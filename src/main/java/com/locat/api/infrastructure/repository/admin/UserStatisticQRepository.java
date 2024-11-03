package com.locat.api.infrastructure.repository.admin;

import com.locat.api.domain.admin.dto.AdminUserStatDto;
import com.locat.api.domain.user.entity.User;

public interface UserStatisticQRepository {

  AdminUserStatDto getUserStat(User user);
}
