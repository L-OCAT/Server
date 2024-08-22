package com.locat.api.domain.geo.found.dto.response;

import com.locat.api.domain.geo.found.entity.FoundItem;
import lombok.Builder;

/**
 * 습득물 상세 조회 응답 DTO
 *
 * @param id 습득물 ID
 * @param category 카테고리명
 * @param color 색상 HEX 코드
 * @param name 습득물 이름
 * @param description 습득물 설명
 * @param custodyLocation 보관 장소
 * @param imageUrl 이미지 URL
 * @param foundAt 습득 일시
 * @param createdAt 등록 일시
 * @param updatedAt 수정 일시
 */
@Builder
public record FoundItemDetailResponse(
    Long id,
    String category,
    String color,
    String name,
    String description,
    String custodyLocation,
    String imageUrl,
    String foundAt,
    String createdAt,
    String updatedAt) {

  public static FoundItemDetailResponse fromEntity(FoundItem foundItem) {
    return FoundItemDetailResponse.builder()
        .id(foundItem.getId())
        .category(foundItem.getCategoryName())
        .color(foundItem.getColorType().getHexCode())
        .name(foundItem.getItemName())
        .description(foundItem.getDescription())
        .custodyLocation(foundItem.getCustodyLocation())
        .imageUrl(foundItem.getImageUrl())
        .foundAt(foundItem.getFoundAt().toString())
        .createdAt(foundItem.getCreatedAt().toString())
        .updatedAt(foundItem.getUpdatedAt().toString())
        .build();
  }
}
