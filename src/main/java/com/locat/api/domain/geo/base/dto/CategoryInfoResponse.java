package com.locat.api.domain.geo.base.dto;

import java.util.List;

/**
 * 카테고리 정보 응답 DTO
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리 이름
 * @param parentCategoryId 상위 카테고리 ID (최상위 카테고리인 경우 {@code null})
 * @param parentCategoryName 상위 카테고리 이름 (최상위 카테고리인 경우 {@code null})
 */
public record CategoryInfoResponse(
    Long categoryId, String categoryName, String parentCategoryId, String parentCategoryName) {

  public static List<CategoryInfoResponse> fromList(List<CategoryInfoDto> dtos) {
    return dtos.stream().map(CategoryInfoResponse::from).toList();
  }

  public static CategoryInfoResponse from(CategoryInfoDto dto) {
    return new CategoryInfoResponse(
        dto.categoryId(), dto.categoryName(), dto.parentCategoryId(), dto.parentCategoryName());
  }
}
