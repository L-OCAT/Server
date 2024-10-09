package com.locat.api.domain.geo.found.dto.response;

import com.locat.api.domain.geo.found.entity.FoundItem;
import java.util.Set;
import lombok.Builder;
import org.springframework.data.geo.GeoResult;

/**
 * 위치 기반 습득물 조회 응답 DTO
 *
 * @param id 습득물 ID
 * @param category 카테고리명
 * @param colors 색상명 (최대 2개)
 * @param name 습득물 이름
 * @param description 습득물 설명
 * @param custodyLocation 보관 장소
 * @param imageUrl 이미지 URL
 * @param lng 경도(lng)
 * @param lat 위도(lat)
 * @param distance 기준 좌표로부터 거리 (단위: 미터)
 * @param foundAt 습득 일시
 */
@Builder
public record FoundItemLocationResponse(
    Long id,
    String category,
    Set<String> colors,
    String name,
    String description,
    String custodyLocation,
    String imageUrl,
    Double lng,
    Double lat,
    Double distance,
    String foundAt) {

  public static FoundItemLocationResponse fromEntity(GeoResult<FoundItem> foundItemGeoResult) {
    FoundItem item = foundItemGeoResult.getContent();
    return FoundItemLocationResponse.builder()
        .id(item.getId())
        .category(item.getCategory().getName())
        .colors(item.getColorNames())
        .name(item.getName())
        .description(item.getDescription())
        .custodyLocation(item.getCustodyLocation())
        .imageUrl(item.getImageUrl())
        .lng(item.getLocation().getX())
        .lat(item.getLocation().getY())
        .distance(Math.floor(foundItemGeoResult.getDistance().getValue()))
        .foundAt(item.getFoundAt().toString())
        .build();
  }
}
