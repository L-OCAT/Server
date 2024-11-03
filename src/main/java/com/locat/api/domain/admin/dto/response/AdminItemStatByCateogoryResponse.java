package com.locat.api.domain.admin.dto.response;

import com.locat.api.domain.admin.dto.AdminItemStatByCategoryDto;
import com.locat.api.domain.geo.base.entity.GeoItem;
import java.util.List;

/**
 * 관리자용 대시보드 카테고리별 {@link GeoItem} 등록 통계 정보 응답
 *
 * @param categoryLabels 상위 카테고리 레이블("가방", "전자기기", "지갑" 등)
 * @param lostItemCount 해당 상위 카테고리로 등록된 분실물 수
 * @param foundItemCount 해당 상위 카테고리로 등록된 습득물 수
 */
public record AdminItemStatByCateogoryResponse(
    List<String> categoryLabels, List<Integer> lostItemCount, List<Integer> foundItemCount) {

  public static AdminItemStatByCateogoryResponse from(List<AdminItemStatByCategoryDto> dto) {
    return new AdminItemStatByCateogoryResponse(
        dto.stream().map(AdminItemStatByCategoryDto::categoryName).toList(),
        dto.stream().map(AdminItemStatByCategoryDto::lostItemCount).map(Long::intValue).toList(),
        dto.stream().map(AdminItemStatByCategoryDto::foundItemCount).map(Long::intValue).toList());
  }
}
