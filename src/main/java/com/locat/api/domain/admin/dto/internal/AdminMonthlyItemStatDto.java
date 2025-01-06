package com.locat.api.domain.admin.dto.internal;

import com.locat.api.domain.geo.base.entity.GeoItem;
import java.util.List;

/**
 * 관리자용 대시보드 월별 {@link GeoItem} 등록 통계 정보 응답 DTO
 *
 * @param monthLabels 월별 레이블
 * @param lostItemCount 월별로 등록된 분실물 수
 * @param foundItemCount 월별로 등록된 습득물 수
 */
public record AdminMonthlyItemStatDto(
    List<String> monthLabels,
    List<MonthlyGeoItemStatistics> lostItemCount,
    List<MonthlyGeoItemStatistics> foundItemCount) {}
