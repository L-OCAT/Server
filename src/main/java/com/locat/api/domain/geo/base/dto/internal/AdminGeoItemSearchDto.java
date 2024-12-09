package com.locat.api.domain.geo.base.dto.internal;

import com.locat.api.domain.geo.base.entity.GeoItemType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 관리자 GeoItem 검색 DTO
 *
 * @param id 전체 ID
 * @param itemId 아이템 ID
 * @param geoItemType 아이템 타입
 * @param itemName 이름
 * @param categoryPath 카테고리 경로
 * @param createdAt 생성 일시
 * @param latitude 위도
 * @param longitude 경도
 * @param region1 지역1(시/도)
 * @param region2 지역2(시/군/구)
 * @param region3 지역3(읍/면/동)
 * @param roadAddress 도로명주소
 * @param buildingName 건물명
 */
public record AdminGeoItemSearchDto(
    Long id,
    Long itemId,
    GeoItemType geoItemType,
    String itemName,
    String categoryPath,
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
        queryResult.id(),
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
