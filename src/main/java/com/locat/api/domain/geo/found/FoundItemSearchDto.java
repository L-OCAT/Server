package com.locat.api.domain.geo.found;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;

public record FoundItemSearchDto(Point location, Distance distance) {

  public static FoundItemSearchDto fromRequest(
      Double latitude, Double longitude, Integer radius, String unit) {
    Point location = new Point(longitude, latitude);
    Distance distance = new Distance(radius, Metrics.valueOf(unit));
    return new FoundItemSearchDto(location, distance);
  }
}
