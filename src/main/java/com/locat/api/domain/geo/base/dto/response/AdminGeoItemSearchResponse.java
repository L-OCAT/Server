package com.locat.api.domain.geo.base.dto.response;

import com.locat.api.domain.geo.base.dto.internal.AdminGeoItemSearchDto;
import jakarta.annotation.Nullable;
import lombok.Builder;

/**
 * 관리자 GeoItem 검색 결과 DTO
 *
 * @param id 아이템 ID
 * @param type 구분(분실물/습득물)
 * @param name 등록된 이름
 * @param categoryPath 카테고리 이름("전자기기 > 휴대폰" 포맷)
 * @param createdAt 등록일
 * @param lat 위도
 * @param lng 경도
 * @param region1 지역1(시/도)
 * @param region2 지역2(시/군/구)
 * @param region3 지역3(읍/면/동)
 * @param roadAddress 도로명주소(Nullable)
 * @param buildingName 건물명(Nullable)
 */
@Builder
public record AdminGeoItemSearchResponse(
    long id,
    String type,
    String name,
    String categoryPath,
    String createdAt,
    double lat,
    double lng,
    String region1,
    String region2,
    String region3,
    @Nullable String roadAddress,
    @Nullable String buildingName) {

  public static AdminGeoItemSearchResponse from(AdminGeoItemSearchDto searchDto) {
    return AdminGeoItemSearchResponse.builder()
        .id(searchDto.itemId())
        .type(searchDto.geoItemType().name())
        .name(searchDto.itemName())
        .categoryPath(searchDto.categoryPath())
        .createdAt(searchDto.createdAt().toString())
        .lat(searchDto.latitude().doubleValue())
        .lng(searchDto.longitude().doubleValue())
        .region1(searchDto.region1())
        .region2(searchDto.region2())
        .region3(searchDto.region3())
        .roadAddress(searchDto.roadAddress())
        .buildingName(searchDto.buildingName())
        .build();
  }
}
