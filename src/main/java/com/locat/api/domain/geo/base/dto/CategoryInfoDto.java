package com.locat.api.domain.geo.base.dto;

public record CategoryInfoDto(
    Long categoryId, String categoryName, String parentCategoryId, String parentCategoryName) {}
