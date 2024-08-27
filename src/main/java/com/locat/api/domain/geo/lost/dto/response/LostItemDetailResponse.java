package com.locat.api.domain.geo.lost.dto.response;

import com.locat.api.domain.geo.lost.entity.LostItem;
import lombok.Builder;

/**
 * 분실물 상세 조회 응답 DTO
 *
 * @param id 분실물 ID
 * @param category 카테고리명
 * @param color 색상명
 * @param name 분실물 이름
 * @param description 분실물 설명
 * @param isWillingToPayGratuity 보상금 지급 여부
 * @param gratuity 보상금 비율
 * @param imageUrl 이미지 URL
 */
@Builder
public record LostItemDetailResponse(
    Long id,
    String category,
    String color,
    String name,
    String description,
    Boolean isWillingToPayGratuity,
    Integer gratuity,
    String imageUrl,
    String lostAt,
    String createdAt,
    String updatedAt) {
  public static LostItemDetailResponse fromEntity(LostItem lostItem) {
    return LostItemDetailResponse.builder()
        .id(lostItem.getId())
        .category(lostItem.getCategory().getName())
        .color(lostItem.getColorCode().getName())
        .name(lostItem.getName())
        .description(lostItem.getDescription())
        .isWillingToPayGratuity(lostItem.getIsWillingToPayGratuity())
        .gratuity(lostItem.getGratuity())
        .imageUrl(lostItem.getImageUrl())
        .lostAt(lostItem.getLostAt().toString())
        .createdAt(lostItem.getCreatedAt().toString())
        .updatedAt(lostItem.getUpdatedAt().toString())
        .build();
  }
}
