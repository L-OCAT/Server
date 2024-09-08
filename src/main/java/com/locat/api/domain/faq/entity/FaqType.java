package com.locat.api.domain.faq.entity;

import java.util.Arrays;

public enum FaqType {
  /** 일반 */
  GENERAL,

  USER,
  LOST_FOUND,
  SERVICE,
  MISCELLANEOUS,
  ;

  public static FaqType fromValueOrDefault(final String type) {
    return Arrays.stream(FaqType.values())
      .filter(faq -> faq.name().equalsIgnoreCase(type))
      .findFirst()
      .orElse(GENERAL);
  }
}
