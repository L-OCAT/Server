package com.locat.api.domain.geo.found.entity;

import com.locat.api.domain.geo.base.entity.GeoItemStatusType;

public enum FoundItemStatusType implements GeoItemStatusType {
  /** 등록됨 */
  REGISTERED,
  /** 분실물과 매칭됨 */
  MATCHED,
  /** 소유자에게 반환됨 */
  RETURNED,
  /** 소유자에게 반환되지 않음 */
  NOT_RETURNED,
  /** 등록자가 삭제함 */
  DELETED
}
