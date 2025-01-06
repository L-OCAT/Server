package com.locat.api.domain.geo.base.dto.internal;

import com.locat.api.domain.geo.base.entity.GeoItem;
import com.locat.api.domain.geo.base.entity.GeoItemAddress;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;

/**
 * Admin Item 상세보기 페이지 DTO
 *
 * @param id 전체 Id
 * @param itemId 아이템 id
 * @param username 작성자 닉네임
 * @param itemType 아이템 타입 (LOST, FOUND)
 * @param itemName 아이템 이름
 * @param imageUrl 아이템 이미지 url
 * @param status 아이템 상태 (REGISTERED, DELETED...)
 * @param colorNames 아이템 색상
 * @param lat 위도
 * @param lng 경도
 * @param region1 지역1(시/도)
 * @param region2 지역2(시/군/구)
 * @param region3 지역3(읍/면/동)
 * @param roadAddress 도로명주소(Nullable)
 * @param buildingName 건물명(Nullable)
 * @param categoryPath 카테고리 경로
 * @param createdAt 등록일
 */
@Builder
public record AdminGeoItemDetailDto(
    Long id,
    Long itemId,
    String username,
    String itemType,
    String itemName,
    String imageUrl,
    String status,
    Set<String> colorNames,
    double lat,
    double lng,
    String region1,
    String region2,
    String region3,
    String roadAddress,
    String buildingName,
    String categoryPath,
    LocalDateTime createdAt) {

  public static AdminGeoItemDetailDto of(
      GeoItem geoItem, GeoItemAddress geoItemAddress, CategoryInfoDto categoryInfoDto) {
    return AdminGeoItemDetailDto.builder()
        .id(geoItemAddress.getId())
        .itemId(geoItem.getId())
        .username(geoItem.getUser().getNickname())
        .itemType(geoItemAddress.getItemType().name())
        .itemName(geoItem.getName())
        .imageUrl(geoItem.getImageUrl())
        .status(geoItem.getStatusType().name())
        .colorNames(geoItem.getColorNames())
        .lat(geoItemAddress.getLatitude().doubleValue())
        .lng(geoItemAddress.getLongitude().doubleValue())
        .region1(geoItemAddress.getRegion1())
        .region2(geoItemAddress.getRegion2())
        .region3(geoItemAddress.getRegion3())
        .roadAddress(geoItemAddress.getRoadAddress())
        .buildingName(geoItemAddress.getBuildingName())
        .categoryPath(categoryInfoDto.toCategoryPath())
        .createdAt(geoItem.getCreatedAt())
        .build();
  }
}
