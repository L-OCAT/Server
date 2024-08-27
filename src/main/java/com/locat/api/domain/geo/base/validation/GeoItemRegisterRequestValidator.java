package com.locat.api.domain.geo.base.validation;

import com.locat.api.global.validation.LocatConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public abstract class GeoItemRegisterRequestValidator<A extends Annotation, T>
    extends LocatConstraintValidator<A, T> {

  /** 위도, 경도 정규표현식 */
  protected static final Pattern LATITUDE_LONGITUDE_PATTERN =
      Pattern.compile("^-?([1-8]?\\d(\\.\\d{1,6})?|90(\\.0{1,6})?)$");

  /** 좌표 조건 위반 시 커스텀 메시지 */
  protected static final String COORDINATES_CONSTRAINT_VIOLATION_MESSAGE = "위도, 경도 값을 올바르게 입력해주세요";

  /**
   * 분실물 & 습득물 등록 요청 객체의 공통 필드에 대한 유효성을 검사합니다.
   *
   * @param request 분실물 또는 습득물 등록 요청 객체
   * @param context ConstraintValidatorContext
   * @return 요청이 모든 유효성 검사를 통과하면 {@code true}, 그렇지 않으면 {@code false}
   */
  @Override
  public boolean isValid(T request, ConstraintValidatorContext context) {
    if (!this.validateCoordinates(request)) {
      super.setCustomViolationMessage(
          context, COORDINATES_CONSTRAINT_VIOLATION_MESSAGE, "lat or lng");
      return false;
    }
    return true;
  }

  /** 좌표 유효성 검사 */
  protected abstract boolean validateCoordinates(T request);
}
