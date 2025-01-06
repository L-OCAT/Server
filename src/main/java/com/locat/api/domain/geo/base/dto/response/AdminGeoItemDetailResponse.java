package com.locat.api.domain.geo.base.dto.response;

import com.locat.api.domain.geo.base.dto.internal.AdminGeoItemDetailDto;
import com.locat.api.domain.geo.base.dto.internal.CategoryInfoDto;
import com.locat.api.domain.geo.base.entity.GeoItem;
import com.locat.api.domain.geo.base.entity.GeoItemAddress;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.lost.entity.LostItem;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;

/**
 * Admin Item 상세보기 페이지 응답 DTO
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
public record AdminGeoItemDetailResponse(
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
    String categoryPath,
    LocalDateTime createdAt) {

  public static AdminGeoItemDetailResponse from(AdminGeoItemDetailDto dto) {
    return AdminGeoItemDetailResponse.builder()
            .id(dto.id())
            .itemId(dto.itemId())
            .username(dto.username())
            .itemType(dto.itemType())
            .itemName(dto.itemName())
            .imageUrl(dto.imageUrl())
            .status(dto.status())
            .colorNames(dto.colorNames())
            .lat(dto.lat())
            .lng(dto.lng())
            .region1(dto.region1())
            .region2(dto.region2())
            .region3(dto.region3())
            .roadAddress(dto.roadAddress())
            .buildingName(dto.buildingName())
            .categoryPath(dto.categoryPath())
            .createdAt(dto.createdAt())
            .build();
  }
}
