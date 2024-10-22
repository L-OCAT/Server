package com.locat.api.domain.admin.dto.response;

import com.locat.api.domain.admin.dto.AdminUserLostItemStatDto;

public record AdminUserLostItemStatResponse(
    Long id, String name, String imageUrl, String categoryName, String status, String createdAt) {
  public static AdminUserLostItemStatResponse from(
      AdminUserLostItemStatDto adminUserLostItemStatDto) {
    return new AdminUserLostItemStatResponse(
        adminUserLostItemStatDto.id(),
        adminUserLostItemStatDto.name(),
        adminUserLostItemStatDto.imageUrl(),
        adminUserLostItemStatDto.categoryName(),
        adminUserLostItemStatDto.status().name(),
        adminUserLostItemStatDto.createdAt().toString());
  }
}
