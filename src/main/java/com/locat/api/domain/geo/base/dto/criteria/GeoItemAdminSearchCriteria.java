package com.locat.api.domain.geo.base.dto.criteria;

import com.locat.api.domain.geo.base.entity.GeoItemType;
import java.time.LocalDate;

/**
 * 관리자 GeoItem 검색 조건 DTO
 *
 * @param itemType 분실물/습득물 타입(전체/분실물/습득물)
 * @param itemName LIKE 검색 할 아이템 이름
 * @param region1 지역1(시/도)
 * @param region2 지역2(시/군/구)
 * @param region3 지역3(읍/면/동)
 * @param categoryId 카테고리 ID
 * @param from 등록일 검색 시작일
 * @param to 등록일 검색 종료일
 */
public record GeoItemAdminSearchCriteria(
    GeoItemType itemType,
    String itemName,
    String region1,
    String region2,
    String region3,
    Long categoryId,
    LocalDate from,
    LocalDate to) {

  public static GeoItemAdminSearchCriteria of(
      String itemType,
      String itemName,
      String region1,
      String region2,
      String region3,
      Long categoryId,
      LocalDate from,
      LocalDate to) {
    return new GeoItemAdminSearchCriteria(
        itemType == null ? null : GeoItemType.fromValue(itemType),
        itemName,
        region1,
        region2,
        region3,
        categoryId,
        from,
        to);
  }
}
