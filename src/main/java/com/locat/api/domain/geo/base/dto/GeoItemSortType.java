package com.locat.api.domain.geo.base.dto;

import java.util.Arrays;

/** 분실물 & 습득물 조회 시, 정렬 조건 타입 */
public enum GeoItemSortType {
  FOUND_AT_ASC,
  FOUND_AT_DESC,
  LOST_AT_ASC,
  LOST_AT_DESC,
  CREATED_AT_ASC,
  CREATED_AT_DESC;

  public static GeoItemSortType toEnum(String sort) {
    return Arrays.stream(GeoItemSortType.values())
        .filter(sortType -> sortType.name().equalsIgnoreCase(sort))
        .findFirst()
        .orElse(CREATED_AT_DESC);
  }
  
  public boolean isCreatedAtAsc() {
    return this == CREATED_AT_ASC;
  }

  public boolean isFoundAtAsc() {
    return this == FOUND_AT_ASC;
  }

  public boolean isFoundAtDesc() {
    return this == FOUND_AT_DESC;
  }

  public boolean isLostAtAsc() {
    return this == LOST_AT_ASC;
  }

  public boolean isLostAtDesc() {
    return this == LOST_AT_DESC;
  }
}
