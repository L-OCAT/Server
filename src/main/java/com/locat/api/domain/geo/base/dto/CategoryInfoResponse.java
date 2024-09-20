package com.locat.api.domain.geo.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 카테고리 정보 응답 DTO
 *
 * @param id 카테고리 ID
 * @param name 카테고리 이름
 * @param parentId 상위 카테고리 ID (최상위 카테고리인 경우 {@code null})
 * @param parentName 상위 카테고리 이름 (최상위 카테고리인 경우 {@code null})
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryInfoResponse(Long id, String name, Long parentId, String parentName) {

  public static CategoryInfoResponse toResponse(CategoryInfoDto dto) {
    return new CategoryInfoResponse(
        dto.categoryId(), dto.categoryName(), dto.parentCategoryId(), dto.parentCategoryName());
  }
}
