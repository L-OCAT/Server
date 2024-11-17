package com.locat.api.domain.geo.base.validation.impl;

import com.locat.api.domain.geo.base.utils.GeoUtils;
import com.locat.api.domain.geo.base.validation.FoundItemValidation;
import com.locat.api.domain.geo.base.validation.GeoItemRegisterRequestValidator;
import com.locat.api.domain.geo.found.dto.request.FoundItemRegisterRequest;

public class FoundItemRegisterRequestValidator
    extends GeoItemRegisterRequestValidator<FoundItemValidation, FoundItemRegisterRequest> {

  @Override
  protected boolean validateCoordinates(FoundItemRegisterRequest request) {
    return LATITUDE_PATTERN.matcher(request.lat().toString()).matches()
        && LONGITUDE_PATTERN.matcher(request.lng().toString()).matches();
  }

  @Override
  protected boolean validateCoordinatesInKorea(FoundItemRegisterRequest request) {
    return GeoUtils.isInKorea(request.lat(), request.lng());
  }

  @Override
  protected boolean validateColorCodesExist(FoundItemRegisterRequest request) {
    return !request.colorIds().isEmpty();
  }

  @Override
  protected boolean validateColorCodesLimit(FoundItemRegisterRequest request) {
    return request.colorIds().size() <= MAX_COLOR_CODES_SIZE;
  }
}
