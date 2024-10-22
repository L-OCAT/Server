package com.locat.api.domain.admin.dto;

import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.found.entity.FoundItemStatusType;
import java.time.LocalDateTime;

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
