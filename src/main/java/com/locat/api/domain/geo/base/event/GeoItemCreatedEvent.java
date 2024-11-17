package com.locat.api.domain.geo.base.event;

import com.locat.api.domain.geo.base.entity.GeoItem;
import com.locat.api.domain.geo.base.entity.GeoItemType;

public record GeoItemCreatedEvent(GeoItemType geoItemType, GeoItem geoItem) {

  public static GeoItemCreatedEvent of(GeoItemType geoItemType, GeoItem geoItem) {
    return new GeoItemCreatedEvent(geoItemType, geoItem);
  }
}
