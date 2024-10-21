package com.locat.api.infrastructure.repository.user;

import com.locat.api.domain.admin.dto.AdminUserStatDto;
import com.locat.api.domain.user.entity.EndUser;

public interface UserQStatisticRepository {

  AdminUserStatDto getUserStat(EndUser user);
}
