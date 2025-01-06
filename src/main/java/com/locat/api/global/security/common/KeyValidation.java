package com.locat.api.global.security.common;

public enum KeyValidation {
  /** API Key 검증 안 함 */
  NONE,
  /** API Key 검증 필요 */
  REQUIRED,
  /** 선택적 검증(헤더에 API Key가 있으면 검증) */
  OPTIONAL,
}
