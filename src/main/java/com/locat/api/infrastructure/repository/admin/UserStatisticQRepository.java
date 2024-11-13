package com.locat.api.infrastructure.repository.admin;

import com.locat.api.domain.admin.dto.AdminUserStatDto;
import com.locat.api.domain.user.entity.User;

public interface UserStatisticQRepository {

  /**
   * 사용자의 통계 정보를 조회합니다.
   *
   * @param user 정보를 조회할 사용자
   * @return 사용자의 통계 정보
   */
  AdminUserStatDto getUserStat(User user);
}
