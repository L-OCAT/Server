package com.locat.api.domain.geo.base.dto.response;

import com.locat.api.domain.geo.base.entity.GeoItem;
import com.locat.api.domain.geo.base.entity.GeoItemAddress;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.lost.entity.LostItem;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;

/**
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
 * @param createdAt 등록일
 */
@Builder
public record GeoItemDetailResponse(
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
    @Nullable String roadAddress,
    @Nullable String buildingName,
    LocalDateTime createdAt) {

  public static GeoItemDetailResponse from(GeoItem geoItem, GeoItemAddress geoItemAddress) {
    return GeoItemDetailResponse.builder()
        .id(geoItemAddress.getId())
        .itemId(geoItem.getId())
        .username(geoItem.getUser().getNickname())
        .itemType(geoItemAddress.getItemType().name())
        .itemName(geoItem.getName())
        .imageUrl(geoItem.getImageUrl())
        .status(
            geoItem instanceof FoundItem foundItem
                ? foundItem.getStatusType().name()
                : geoItem instanceof LostItem lostItem ? lostItem.getStatusType().name() : null)
        .colorNames(
            geoItem instanceof FoundItem foundItem
                ? foundItem.getColorNames()
                : geoItem instanceof LostItem lostItem ? lostItem.getColorNames() : null)
        .lat(geoItemAddress.getLatitude().doubleValue())
        .lng(geoItemAddress.getLongitude().doubleValue())
        .region1(geoItemAddress.getRegion1())
        .region2(geoItemAddress.getRegion2())
        .region3(geoItemAddress.getRegion3())
        .roadAddress(geoItemAddress.getRoadAddress())
        .buildingName(geoItemAddress.getBuildingName())
        .createdAt(geoItem.getCreatedAt())
        .build();
  }
}
