package com.locat.api.domain.geo.base.dto.internal;

import com.locat.api.domain.geo.base.entity.GeoItemType;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 관리자 GeoItem 검색 쿼리 결과 DTO
 *
 * @param itemId 조회된 아이템 ID
 * @param geoItemType 아이템 타입(분실물/습득물)
 * @param itemName 이름
 * @param categoryId 카테고리 ID
 * @param createdAt 분실/습득 일시
 * @param latitude 위도
 * @param longitude 경도
 * @param region1 지역1(시/도)
 * @param region2 지역2(시/군/구)
 * @param region3 지역3(읍/면/동)
 * @param roadAddress 도로명주소
 * @param buildingName 건물명
 */
public record AdminGeoItemSearchQueryResult(
    Long itemId,
    GeoItemType geoItemType,
    String itemName,
    Long categoryId,
    LocalDateTime createdAt,
    BigDecimal latitude,
    BigDecimal longitude,
    String region1,
    String region2,
    String region3,
    @Nullable String roadAddress,
    @Nullable String buildingName) {}
// 도로명주소
