package com.locat.api.domain.geo.lost.dto.response;

import com.locat.api.domain.geo.lost.entity.LostItem;
import lombok.Builder;

@Builder
public record LostItemDetailResponse(
    Long id,
    String category,
    String color,
    String name,
    String description,
    Boolean isWillingToPayGratuity,
    Integer gratuity,
    String imageUrl) {
  public static LostItemDetailResponse fromEntity(LostItem lostItem) {
    return LostItemDetailResponse.builder()
        .id(lostItem.getId())
        .category(lostItem.getCategoryName())
        .color(lostItem.getColorType().getHexCode())
        .name(lostItem.getItemName())
        .description(lostItem.getDescription())
        .isWillingToPayGratuity(lostItem.getIsWillingToPayGratuity())
        .gratuity(lostItem.getGratuity())
        .imageUrl(lostItem.getImageUrl())
        .build();
  }
}
