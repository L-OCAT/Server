package com.locat.api.domain.geo.lost.entity;

public enum LostItemStatusType {
  /** 등록됨 */
  REGISTERED,
  /** 주인이 찾음 */
  CLAIMED,
  /** 주인에게 반환됨 */
  RETURNED,
  /** 주인에게 반환되지 않음 */
  NOT_RETURNED,
  /** 등록자가 삭제함 */
  DELETED
}
