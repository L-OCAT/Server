package com.locat.api.domain.admin.dto.internal;

import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.found.entity.FoundItemStatusType;
import java.time.LocalDateTime;

/**
 * 관리자용 사용자별 습득물 통계 정보 응답 DTO
 *
 * @param id 습득물 ID
 * @param name 습득물 이름
 * @param imageUrl 습득물 이미지 URL
 * @param categoryName 습득물 카테고리명
 * @param status 습득물 상태
 * @param createdAt 습득물 등록일시
 */
public record AdminUserFoundItemStatDto(
    Long id,
    String name,
    String imageUrl,
    String categoryName,
    FoundItemStatusType status,
    LocalDateTime createdAt) {

  public static AdminUserFoundItemStatDto fromEntity(FoundItem foundItem) {
    return new AdminUserFoundItemStatDto(
        foundItem.getId(),
        foundItem.getName(),
        foundItem.getImageUrl(),
        foundItem.getCategory().getName(),
        foundItem.getStatusType(),
        foundItem.getCreatedAt());
  }
}
