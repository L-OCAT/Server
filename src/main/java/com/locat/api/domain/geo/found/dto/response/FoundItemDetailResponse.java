package com.locat.api.domain.geo.found.dto.response;

import com.locat.api.domain.geo.found.entity.FoundItem;
import java.util.Set;
import lombok.Builder;

/**
 * 습득물 상세 조회 응답 DTO
 *
 * @param id 습득물 ID
 * @param category 카테고리명
 * @param colors 색상명 (최대 2개)
 * @param name 습득물 이름
 * @param description 습득물 설명
 * @param custodyLocation 보관 장소
 * @param imageUrl 이미지 URL
 * @param lat 위도(latitude)
 * @param lng 경도(longitude)
 * @param foundAt 습득 일시
 * @param createdAt 등록 일시
 * @param updatedAt 수정 일시
 */
@Builder
public record FoundItemDetailResponse(
    long id,
    String category,
    Set<String> colors,
    String name,
    String description,
    String custodyLocation,
    String imageUrl,
    double lat,
    double lng,
    String foundAt,
    String createdAt,
    String updatedAt) {

  public static FoundItemDetailResponse fromEntity(FoundItem foundItem) {
    return FoundItemDetailResponse.builder()
        .id(foundItem.getId())
        .category(foundItem.getCategory().getName())
        .colors(foundItem.getColorNames())
        .name(foundItem.getName())
        .description(foundItem.getDescription())
        .custodyLocation(foundItem.getCustodyLocation())
        .imageUrl(foundItem.getImageUrl())
        .lat(foundItem.getLocation().getY())
        .lng(foundItem.getLocation().getX())
        .foundAt(foundItem.getFoundAt().toString())
        .createdAt(foundItem.getCreatedAt().toString())
        .updatedAt(foundItem.getUpdatedAt().toString())
        .build();
  }
}
