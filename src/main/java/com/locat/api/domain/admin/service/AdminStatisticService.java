package com.locat.api.domain.admin.service;

import com.locat.api.domain.admin.dto.*;
import com.locat.api.domain.geo.base.entity.GeoItem;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import java.util.List;

public interface AdminStatisticService {

  /**
   * 대시보드 통계 정보 조회
   *
   * @return 대시보드 통계 정보
   */
  AdminDashboardSummaryDto getSummary();

  /**
   * 대시보드의 월별 {@link GeoItem} 등록 통계 정보 조회
   *
   * @return 월별 통계 정보
   */
  AdminMonthlyItemStatDto getMonthlyItemStat();

  /**
   * 대시보드의 카테고리별 {@link GeoItem} 등록 통계 정보 조회
   *
   * @return 카테고리별 통계 정보
   */
  List<AdminItemStatByCategoryDto> getStatByCategory();

  /**
   * 일반 사용자의 통계 정보 조회
   *
   * @param userId 조회할 사용자 ID
   * @return 사용자 통계 정보
   * @throws NoSuchEntityException 주어진 ID에 해당하는 사용자가 없는 경우
   */
  AdminUserStatDto getEndUserStat(Long userId);

  /**
   * 사용자의 습득물 등록 통계 정보 조회
   *
   * @param userId 조회할 사용자 ID
   * @return 사용자의 습득물 등록 통계 정보
   */
  List<AdminUserFoundItemStatDto> getUserFoundItemStat(Long userId);

  /**
   * 사용자의 습득물 등록 통계 정보 조회
   *
   * @param userId 조회할 사용자 ID
   * @return 사용자의 습득물 등록 통계 정보
   */
  List<AdminUserLostItemStatDto> getUserLostItemStat(Long userId);
}
