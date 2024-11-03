package com.locat.api.domain.admin.dto;

/**
 * 관리자용 대시보드 카테고리별 분실물/습득물 통계 정보
 *
 * @param categoryName 상위 카테고리명
 * @param lostItemCount 분실물 수
 * @param foundItemCount 습득물 수
 */
public record AdminItemStatByCategoryDto(
    String categoryName, Long lostItemCount, Long foundItemCount) {}
