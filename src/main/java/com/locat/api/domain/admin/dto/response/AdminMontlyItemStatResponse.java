package com.locat.api.domain.admin.dto.response;

import com.locat.api.domain.admin.dto.AdminMonthlyItemStatDto;
import com.locat.api.domain.admin.dto.inner.MonthlyGeoItemStatistics;
import com.locat.api.domain.geo.base.entity.GeoItem;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 관리자용 대시보드 월별 {@link GeoItem} 등록 통계 정보 응답
 *
 * @param lostItemCount 월별 분실물 등록 수
 * @param foundItemCount 월별 습득물 등록 수
 */
public record AdminMontlyItemStatResponse(
    List<String> monthLabels, List<Integer> lostItemCount, List<Integer> foundItemCount) {

  public static AdminMontlyItemStatResponse from(AdminMonthlyItemStatDto dto) {
    List<String> labels = dto.monthLabels();
    Map<String, Integer> lostMap = createCountMap(dto.lostItemCount());
    Map<String, Integer> foundMap = createCountMap(dto.foundItemCount());
    // Chart.js 표현을 위한 포맷으로 변환 & 데이터 빈 구간은 0으로 채움
    return new AdminMontlyItemStatResponse(
        labels,
        labels.stream().map(month -> lostMap.getOrDefault(month, 0)).toList(),
        labels.stream().map(month -> foundMap.getOrDefault(month, 0)).toList());
  }

  private static Map<String, Integer> createCountMap(List<MonthlyGeoItemStatistics> counts) {
    return counts.stream()
        .collect(
            Collectors.toMap(MonthlyGeoItemStatistics::month, MonthlyGeoItemStatistics::count));
  }
}
