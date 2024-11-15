package com.locat.api.domain.geo.base.dto.criteria;

import org.locationtech.jts.geom.Point;
import org.springframework.data.geo.Distance;

/** 분실물 & 습득물 공통 검색 조건 인터페이스 */
public interface GeoItemSearchCriteria {

  Boolean getOnlyMine();

  Point getLocation();

  Distance getDistance();
}
