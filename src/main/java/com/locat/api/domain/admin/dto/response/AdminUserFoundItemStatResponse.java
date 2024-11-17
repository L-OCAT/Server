package com.locat.api.domain.admin.dto.response;

import com.locat.api.domain.admin.dto.internal.AdminUserFoundItemStatDto;

public record AdminUserFoundItemStatResponse(
    Long id, String name, String imageUrl, String categoryName, String status, String createdAt) {

  public static AdminUserFoundItemStatResponse from(
      AdminUserFoundItemStatDto adminUserFoundItemStatDto) {
    return new AdminUserFoundItemStatResponse(
        adminUserFoundItemStatDto.id(),
        adminUserFoundItemStatDto.name(),
        adminUserFoundItemStatDto.imageUrl(),
        adminUserFoundItemStatDto.categoryName(),
        adminUserFoundItemStatDto.status().name(),
        adminUserFoundItemStatDto.createdAt().toString());
  }
}
