package com.locat.api.domain.geo.base.dto;

/**
 * 카테고리 정보 DTO
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리 이름
 * @param parentCategoryId 상위 카테고리 ID (최상위 카테고리인 경우 {@code null})
 * @param parentCategoryName 상위 카테고리 이름 (최상위 카테고리인 경우 {@code null})
 */
public record CategoryInfoDto(
    Long categoryId, String categoryName, Long parentCategoryId, String parentCategoryName) {}
