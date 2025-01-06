package com.locat.api.domain.geo.base.validation.impl;

import com.locat.api.domain.geo.base.utils.GeoUtils;
import com.locat.api.domain.geo.base.validation.GeoItemRegisterRequestValidator;
import com.locat.api.domain.geo.base.validation.LostItemValidation;
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
  private static final String GRATUITY_RATE_VIOLATION_MESSAGE =
      "Gratuity rate must be a value between 0 and 5%";

  /** 보상금 지급 의사가 있다고 표시했지만, 보상금 비율을 입력하지 않은 경우 커스텀 메시지 */
  private static final String GRATUITY_VALUE_CONSTRAINT_VIOLATION_MESSAGE =
      "If the intent to pay gratuity is indicated, the gratuity value must be provided";

  @Override
  public boolean isValid(LostItemRegisterRequest request, ConstraintValidatorContext context) {
    if (request.isWillingToPayGratuity()) {
      if (Objects.isNull(request.gratuity())) {
        super.setCustomViolationMessage(
            context, GRATUITY_VALUE_CONSTRAINT_VIOLATION_MESSAGE, "gratuity");
        return false;
      }
      if (!this.validateGratuity(request)) {
        super.setCustomViolationMessage(context, GRATUITY_RATE_VIOLATION_MESSAGE, "gratuity");
        return false;
      }
    }
    return super.isValid(request, context);
  }

  @Override
  protected boolean validateCoordinates(LostItemRegisterRequest request) {
    return LATITUDE_PATTERN.matcher(request.lat().toString()).matches()
        && LONGITUDE_PATTERN.matcher(request.lng().toString()).matches();
  }

  @Override
  protected boolean validateCoordinatesInKorea(LostItemRegisterRequest request) {
    return GeoUtils.isInKorea(request.lat(), request.lng());
  }

  @Override
  protected boolean validateColorCodesExist(LostItemRegisterRequest request) {
    return !request.colorIds().isEmpty();
  }

  private boolean validateGratuity(LostItemRegisterRequest request) {
    return Objects.nonNull(request.gratuity())
        && GRATUITY_RATE_PATTERN.matcher(request.gratuity().toString()).matches();
  }

  @Override
  protected boolean validateColorCodesLimit(LostItemRegisterRequest request) {
    return request.colorIds().size() <= MAX_COLOR_CODES_SIZE;
  }
}
