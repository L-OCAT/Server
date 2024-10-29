package com.locat.api.infrastructure.repository.user;

import com.locat.api.domain.admin.dto.AdminUserStatDto;
import com.locat.api.domain.user.entity.User;

public interface UserQStatisticRepository {

  AdminUserStatDto getUserStat(User user);
}
