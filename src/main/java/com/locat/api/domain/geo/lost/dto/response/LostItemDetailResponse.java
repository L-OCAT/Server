package com.locat.api.domain.geo.lost.dto.response;

import com.locat.api.domain.geo.lost.entity.LostItem;
import java.util.Set;
import lombok.Builder;

/**
 * 분실물 상세 조회 응답 DTO
 *
 * @param id 분실물 ID
 * @param category 카테고리명
 * @param colors 색상명 (최대 2개)
 * @param name 분실물 이름
 * @param description 분실물 설명
 * @param isWillingToPayGratuity 보상금 지급 여부
 * @param gratuity 보상금 비율
 * @param imageUrl 이미지 URL
 * @param lat 위도(latitude)
 * @param lng 경도(longitude)
 * @param lostAt 분실 일시
 * @param createdAt 등록 일시
 * @param updatedAt 수정 일시
 */
@Builder
public record LostItemDetailResponse(
    long id,
    String category,
    Set<String> colors,
    String name,
    String description,
    Boolean isWillingToPayGratuity,
    Integer gratuity,
    String imageUrl,
    double lat,
    double lng,
    String lostAt,
    String createdAt,
    String updatedAt) {
  public static LostItemDetailResponse fromEntity(LostItem lostItem) {
    return LostItemDetailResponse.builder()
        .id(lostItem.getId())
        .category(lostItem.getCategory().getName())
        .colors(lostItem.getColorNames())
        .name(lostItem.getName())
        .description(lostItem.getDescription())
        .isWillingToPayGratuity(lostItem.getIsWillingToPayGratuity())
        .gratuity(lostItem.getGratuity())
        .imageUrl(lostItem.getImageUrl())
        .lat(lostItem.getLocation().getY())
        .lng(lostItem.getLocation().getX())
        .lostAt(lostItem.getLostAt().toString())
        .createdAt(lostItem.getCreatedAt().toString())
        .updatedAt(lostItem.getUpdatedAt().toString())
        .build();
  }
}
