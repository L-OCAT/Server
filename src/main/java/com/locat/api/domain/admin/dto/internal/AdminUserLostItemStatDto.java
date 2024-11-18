package com.locat.api.domain.admin.dto.internal;

import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.geo.lost.entity.LostItemStatusType;
import java.time.LocalDateTime;

/**
 * 관리자용 사용자별 분실물 통계 정보 응답 DTO
 *
 * @param id 분실물 ID
 * @param name 분실물 이름
 * @param imageUrl 분실물 이미지 URL
 * @param categoryName 분실물 카테고리명
 * @param status 분실물 상태
 * @param createdAt 분실물 등록일시
 */
public record AdminUserLostItemStatDto(
    Long id,
    String name,
    String imageUrl,
    String categoryName,
    LostItemStatusType status,
    LocalDateTime createdAt) {

  public static AdminUserLostItemStatDto fromEntity(LostItem lostItem) {
    return new AdminUserLostItemStatDto(
        lostItem.getId(),
        lostItem.getName(),
        lostItem.getImageUrl(),
        lostItem.getCategory().getName(),
        lostItem.getStatusType(),
        lostItem.getCreatedAt());
  }
}
