package com.locat.api.domain.terms.entity;

import com.locat.api.global.exception.InvalidParameterException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TermsType {
  /** 서비스 이용약관 */
  TERMS_OF_SERVICE("서비스 이용약관"),
  /** 개인정보 처리방침 */
  PRIVACY_POLICY("개인정보 처리방침"),
  /** 만 14세 이상 */
  OVER_14_POLICY("만 14세 이상 확인"),
  /** 위치정보 제공 동의 */
  LOCATION_POLICY("위치정보 제공 동의"),
  /** 마케팅 정보 수신 동의 */
  MARKETING_POLICY("마케팅 정보 수신 동의"),
  /** (카카오톡) 마케팅 정보 수신 동의 */
  MARKETING_KAKAO_POLICY("카카오톡 마케팅 정보 수신 동의");

  private final String title;

  public static TermsType fromValue(final String type) {
    return Arrays.stream(TermsType.values())
        .filter(terms -> terms.name().equalsIgnoreCase(type))
        .findFirst()
        .orElseThrow(() -> new InvalidParameterException("No Such Terms Type exists"));
  }
}
