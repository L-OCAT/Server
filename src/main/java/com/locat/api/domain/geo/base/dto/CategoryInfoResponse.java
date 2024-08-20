package com.locat.api.domain.geo.base.dto;

import java.util.List;

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
