package com.locat.api.domain.admin.dto.response;

import com.locat.api.domain.admin.dto.internal.AdminUserLostItemStatDto;

/**
 * 관리자용 사용자 분실물 통계 정보 응답 DTO
 *
 * @param id 분실물 ID
 * @param name 분실물 이름
 * @param imageUrl 분실물 이미지 URL
 * @param categoryName 분실물 카테고리명
 * @param status 분실물 상태
 * @param createdAt 분실물 등록일
 */
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
