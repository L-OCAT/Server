package com.locat.api.domain.geo.base.validation;

import com.locat.api.global.validation.LocatConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public abstract class GeoItemRegisterRequestValidator<A extends Annotation, T>
    extends LocatConstraintValidator<A, T> {

  /** HEX 색상 코드 정규표현식 */
  protected static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{6}$");

  /** 위도, 경도 정규표현식 */
  protected static final Pattern LATITUDE_LONGITUDE_PATTERN =
      Pattern.compile("^-?([1-8]?\\d(\\.\\d{1,6})?|90(\\.0{1,6})?)$");

  /** 색상 코드 조건 위반 시 커스텀 메시지 */
  protected static final String HEX_COLOR_CONSTRAINT_VIOLATION_MESSAGE =
      "HEX 색상 코드 형식에 맞게 입력해야 합니다";

  /** 좌표 조건 위반 시 커스텀 메시지 */
  protected static final String COORDINATES_CONSTRAINT_VIOLATION_MESSAGE = "위도, 경도 값을 올바르게 입력해주세요";

  /** 카테고리 이름 조건 위반 시 커스텀 메시지 */
  protected static final String CATEGORY_NAME_CONSTRAINT_VIOLATION_MESSAGE =
      "카테고리를 직접 입력하는 경우, 반드시 카테고리 이름을 입력해야 합니다";

  /**
   * 분실물 & 습득물 등록 요청 객체의 공통 필드에 대한 유효성을 검사합니다.
   *
   * @param request 분실물 또는 습득물 등록 요청 객체
   * @param context ConstraintValidatorContext
   * @return 요청이 모든 유효성 검사를 통과하면 {@code true}, 그렇지 않으면 {@code false}
   */
  @Override
  public boolean isValid(T request, ConstraintValidatorContext context) {
    if (!this.validateColorHexCode(request)) {
      super.setCustomViolationMessage(
          context, HEX_COLOR_CONSTRAINT_VIOLATION_MESSAGE, "colorHexCode");
      return false;
    }
    if (!this.validateCoordinates(request)) {
      super.setCustomViolationMessage(
          context, COORDINATES_CONSTRAINT_VIOLATION_MESSAGE, "latitude or longitude");
      return false;
    }
    if (!this.validateCategoryNameIfCustom(request)) {
      super.setCustomViolationMessage(
          context, CATEGORY_NAME_CONSTRAINT_VIOLATION_MESSAGE, "categoryName");
      return false;
    }
    return true;
  }

  /** 사용자가 직접 카테고리를 입력한 경우, 카테고리 이름이 입력되었는지 검증 */
  protected abstract boolean validateCategoryNameIfCustom(T request);

  /** 색상 코드 유효성 검사 */
  protected abstract boolean validateColorHexCode(T request);

  /** 좌표 유효성 검사 */
  protected abstract boolean validateCoordinates(T request);
}
