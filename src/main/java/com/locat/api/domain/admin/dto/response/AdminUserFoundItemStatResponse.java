package com.locat.api.domain.admin.dto.response;

import com.locat.api.domain.admin.dto.internal.AdminUserFoundItemStatDto;

/**
 * 관리자용 사용자별 습득물 통계 정보 응답 DTO
 *
 * @param id 습득물 ID
 * @param name 습득물 이름
 * @param imageUrl 습득물 이미지 URL
 * @param categoryName 상위 카테고리명
 * @param status 습득물 상태
 * @param createdAt 습득일
 */
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
