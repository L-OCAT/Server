package com.locat.api.infrastructure.repository.admin;

import com.locat.api.domain.admin.dto.AdminDashboardSummaryDto;
import com.locat.api.domain.admin.dto.AdminItemStatByCategoryDto;
import com.locat.api.domain.admin.dto.AdminMonthlyItemStatDto;
import java.util.List;

/** 관리자 대시보드 정보 QueryDSL Repository */
public interface AdminDashboardQRepository {

  /**
   * 대시보드 요약 정보(총 사용자, 총 분실물, 총 습득물)를 조회합니다.
   *
   * @return 대시보드 요약 정보
   */
  AdminDashboardSummaryDto getSummary();

  /**
   * 카테고리별 분실물/습득물 통계 정보를 조회합니다.
   *
   * @return 카테고리별 분실물/습득물 통계 정보
   */
  List<AdminItemStatByCategoryDto> getStatByCategory();

  /**
   * 월별 분실물/습득물 통계 정보를 조회합니다.
   *
   * @return 월별 분실물/습득물 통계 정보
   */
  AdminMonthlyItemStatDto getMonthlyItemStat();
}
