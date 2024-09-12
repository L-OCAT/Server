package com.locat.api.domain.terms.entity;

import com.locat.api.global.exception.InvalidParameterException;
import java.util.Arrays;

public enum TermsType {
  /** 서비스 이용약관 */
  TERMS_OF_SERVICE,
  /** 개인정보 처리방침 */
  PRIVACY_POLICY,
  /** 만 14세 이상 */
  OVER_14_POLICY,
  /** 위치정보 제공 동의 */
  LOCATION_POLICY,
  /** 마케팅 정보 수신 동의 */
  MARKETING_POLICY,
  /** (카카오톡) 마케팅 정보 수신 동의 */
  MARKETING_KAKAO_POLICY,
  ;

  public static TermsType fromValue(final String type) {
    return Arrays.stream(TermsType.values())
        .filter(terms -> terms.name().equalsIgnoreCase(type))
        .findFirst()
        .orElseThrow(() -> new InvalidParameterException("No Such Terms Type exists"));
  }
}
