package com.locat.api.domain.user.entity;

/**
 * 사용자 상태 Enum
 */
public enum StatusType {
  /**
   * 활성화 대기중
   */
  PENDING,
  /**
   * 활성화
   */
  ACTIVE,
  /**
   * 비활성화(탈퇴)
   */
  INACTIVE,
  /**
   * 이용 제한
   */
  BANNED
}
