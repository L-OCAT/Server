package com.locat.api.domain.geo.base.validation;

import com.locat.api.domain.geo.base.annotation.FoundItemValidation;
import com.locat.api.domain.geo.found.dto.request.FoundItemRegisterRequest;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class FoundItemRegisterRequestValidator
    extends GeoItemRegisterRequestValidator<FoundItemValidation, FoundItemRegisterRequest> {

  @Override
  public boolean isValid(FoundItemRegisterRequest request, ConstraintValidatorContext context) {
    return super.isValid(request, context);
  }

  @Override
  protected boolean validateNickname(FoundItemRegisterRequest request) {
    return NICKNAME_PATTERN.matcher(request.categoryName()).matches();
  }

  @Override
  protected boolean validateColorHexCode(FoundItemRegisterRequest request) {
    return HEX_COLOR_PATTERN.matcher(request.colorHexCode()).matches();
  }

  @Override
  protected boolean validateCoordinates(FoundItemRegisterRequest request) {
    return LATITUDE_LONGITUDE_PATTERN.matcher(request.latitude().toString()).matches()
        && LATITUDE_LONGITUDE_PATTERN.matcher(request.longitude().toString()).matches();
  }

  @Override
  protected boolean validateCategoryNameIfCustom(FoundItemRegisterRequest request) {
    return Objects.isNull(request.categoryId()) && Objects.nonNull(request.categoryName());
  }
}
