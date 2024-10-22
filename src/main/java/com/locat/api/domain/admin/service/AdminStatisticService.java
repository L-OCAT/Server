package com.locat.api.domain.admin.service;

import com.locat.api.domain.admin.dto.AdminUserFoundItemStatDto;
import com.locat.api.domain.admin.dto.AdminUserLostItemStatDto;
import com.locat.api.domain.admin.dto.AdminUserStatDto;
import com.locat.api.global.exception.NoSuchEntityException;
import java.util.List;

public interface AdminStatisticService {

  /**
   * 일반 사용자의 통계 정보 조회
   *
   * @param userId 조회할 사용자 ID
   * @return 사용자 통계 정보
   * @throws NoSuchEntityException 주어진 ID에 해당하는 사용자가 없는 경우
   */
  AdminUserStatDto getEndUserStat(Long userId);

  List<AdminUserFoundItemStatDto> getUserFoundItemStat(Long userId);

  List<AdminUserLostItemStatDto> getUserLostItemStat(Long userId);
}
