package com.locat.api.domain.geo.lost.dto;

import com.locat.api.domain.geo.base.dto.criteria.GeoItemSearchCriteria;
import com.locat.api.domain.geo.base.utils.GeoUtils;
import lombok.Builder;
import org.locationtech.jts.geom.Point;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;

/**
 * 분실물 검색 조건 DTO
 *
 * @param onlyMine 내가 등록한 분실물만 검색 여부
 * @param location 검색 위치가 될 중심 좌표
 * @param distance 검색 반경
 */
@Builder
public record LostItemSearchDto(Boolean onlyMine, Point location, Distance distance)
    implements GeoItemSearchCriteria {

  @Override
  public Boolean getOnlyMine() {
    return this.onlyMine;
  }

  @Override
  public Point getLocation() {
    return this.location;
  }

  @Override
  public Distance getDistance() {
    return this.distance;
  }

  public static LostItemSearchDto fromRequest(Boolean onlyMine, Point location, Double radius) {
    return LostItemSearchDto.builder()
        .onlyMine(onlyMine)
        .location(location)
        .distance(new Distance(GeoUtils.toKilometer(radius), Metrics.KILOMETERS))
        .build();
  }
}
