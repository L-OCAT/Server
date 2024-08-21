package com.locat.api.domain.geo.found.dto;

import lombok.Builder;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;

@Builder
public record FoundItemSearchDto(Point location, Distance distance) {

  public static FoundItemSearchDto fromRequest(Double latitude, Double longitude, Integer radius) {
    Point location = new Point(longitude, latitude);
    Distance distance = new Distance(radius, Metrics.KILOMETERS);
    return new FoundItemSearchDto(location, distance);
  }
}
