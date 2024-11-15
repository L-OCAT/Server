package com.locat.api.domain.geo.base.dto.internal;

import com.locat.api.domain.geo.base.entity.GeoItemType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminGeoItemSearchDto(
    Long itemId,
    GeoItemType geoItemType,
    String itemName,
    String categoryName,
    LocalDateTime createdAt,
    BigDecimal latitude,
    BigDecimal longitude,
    String region1,
    String region2,
    String region3,
    String roadAddress,
    String buildingName) {

  public static AdminGeoItemSearchDto of(
      AdminGeoItemSearchQueryResult queryResult, CategoryInfoDto categoryInfoDto) {
    return new AdminGeoItemSearchDto(
        queryResult.itemId(),
        queryResult.geoItemType(),
        queryResult.itemName(),
        categoryInfoDto.toCategoryPath(),
        queryResult.createdAt(),
        queryResult.latitude(),
        queryResult.longitude(),
        queryResult.region1(),
        queryResult.region2(),
        queryResult.region3(),
        queryResult.roadAddress(),
        queryResult.buildingName());
  }
}
