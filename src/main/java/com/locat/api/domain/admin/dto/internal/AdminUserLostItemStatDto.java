package com.locat.api.domain.admin.dto.internal;

import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.geo.lost.entity.LostItemStatusType;
import java.time.LocalDateTime;

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
