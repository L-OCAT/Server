package com.locat.api.domain.faq.entity;

import java.util.Arrays;

public enum FaqType {
  /** 공통 */
  GENERAL,
  /** 회원 관련 */
  USER,
  /** 분실물 & 습득물 관련 */
  LOST_FOUND,
  /** 서비스 이용 관련 */
  SERVICE,
  /** 그 외 기타 */
  MISCELLANEOUS,
  ;

  public static FaqType fromValueOrDefault(final String type) {
    return Arrays.stream(FaqType.values())
        .filter(faq -> faq.name().equalsIgnoreCase(type))
        .findFirst()
        .orElse(GENERAL);
  }
}
