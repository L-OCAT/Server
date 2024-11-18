package com.locat.api.domain.geo.lost.dto.response;

import com.locat.api.domain.geo.lost.entity.LostItem;
import java.util.Set;
import lombok.Builder;
import org.springframework.data.geo.GeoResult;

/**
 * 위치 기반 분실물 조회 응답 DTO
 *
 * @param id 분실물 ID
 * @param category 카테고리명
 * @param colors 색상명 (최대 2개)
 * @param name 분실물 이름
 * @param description 분실물 설명
 * @param lng 경도(longitude)
 * @param lat 위도(latitude)
 * @param distance 기준 좌표로부터 거리 (단위: 미터)
 * @param imageUrl 이미지 URL
 * @param lostAt 분실 일시
 */
@Builder
public record LostItemLocationResponse(
    long id,
    String category,
    Set<String> colors,
    String name,
    String description,
    double lng,
    double lat,
    double distance,
    String imageUrl,
    String lostAt) {

  public static LostItemLocationResponse fromEntity(GeoResult<LostItem> lostItem) {
    LostItem item = lostItem.getContent();
    return LostItemLocationResponse.builder()
        .id(item.getId())
        .category(item.getCategory().getName())
        .colors(item.getColorNames())
        .name(item.getName())
        .description(item.getDescription())
        .lng(item.getLocation().getX())
        .lat(item.getLocation().getY())
        .distance(Math.floor(lostItem.getDistance().getValue()))
        .imageUrl(item.getImageUrl())
        .lostAt(item.getLostAt().toString())
        .build();
  }
}
