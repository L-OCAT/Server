package com.locat.api.domain.geo.found;

import lombok.Builder;

@Builder
public record FoundItemInfoResponse(
    Long id,
    String categoryName,
    String colorName,
    String itemName,
    String description,
    String custodyLocation,
    String imageUrl,
    String foundAt,
    String createdAt,
    String updatedAt) {
  public static FoundItemInfoResponse fromEntity(FoundItem foundItem) {}
}
