package com.locat.api.domain.geo.base.validation;

import com.locat.api.domain.geo.base.annotation.LostItemValidation;
import com.locat.api.domain.geo.lost.dto.request.LostItemRegisterRequest;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;

public class LostItemRegisterRequestValidator
    extends GeoItemRegisterRequestValidator<LostItemValidation, LostItemRegisterRequest> {

  /**
   * 보상금 지급 비율 값(백분율, %) 정규표현식
   * <li>0 ~ 5% [법정 최대 보상금 비율]
   */
  private static final Pattern GRATUITY_RATE_PATTERN = Pattern.compile("^[0-5]$");

  /** 보상금 비율 값이 0 ~ 5% 사이의 값이 아닌 경우 커스텀 메시지 */
  private static final String GRATUITY_RATE_VIOLATION_MESSAGE = "보상금 비율은 0 ~ 5% 사이의 값이어야 합니다.";

  /** 보상금 지급 의사가 있다고 표시했지만, 보상금 비율을 입력하지 않은 경우 커스텀 메시지 */
  private static final String GRATUITY_VALUE_CONSRANT_VIOLATION_MESSAGE =
      "보상금 지급 의사가 있다고 표시한 경우, 반드시 보상금 비율을 입력해야 합니다.";

  @Override
  public boolean isValid(LostItemRegisterRequest request, ConstraintValidatorContext context) {
    if (request.isWillingToPayGratuity()) {
      if (Objects.isNull(request.gratuity())) {
        super.setCustomViolationMessage(context, GRATUITY_VALUE_CONSRANT_VIOLATION_MESSAGE);
        return false;
      }
      if (!this.validateGratuity(request)) {
        super.setCustomViolationMessage(context, GRATUITY_RATE_VIOLATION_MESSAGE);
        return false;
      }
    }
    return super.isValid(request, context);
  }

  @Override
  protected boolean validateNickname(LostItemRegisterRequest request) {
    return NICKNAME_PATTERN.matcher(request.categoryName()).matches();
  }

  @Override
  protected boolean validateColorHexCode(LostItemRegisterRequest request) {
    return HEX_COLOR_PATTERN.matcher(request.colorHexCode()).matches();
  }

  @Override
  protected boolean validateCoordinates(LostItemRegisterRequest request) {
    return LATITUDE_LONGITUDE_PATTERN.matcher(request.latitude().toString()).matches()
        && LATITUDE_LONGITUDE_PATTERN.matcher(request.longitude().toString()).matches();
  }

  @Override
  protected boolean validateCategoryNameIfCustom(LostItemRegisterRequest request) {
    return Objects.isNull(request.categoryId()) && Objects.nonNull(request.categoryName());
  }

  private boolean validateGratuity(LostItemRegisterRequest request) {
    return Objects.nonNull(request.gratuity())
        && GRATUITY_RATE_PATTERN.matcher(request.gratuity().toString()).matches();
  }
}