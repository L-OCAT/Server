package com.locat.api.domain.geo.base.service;

import com.locat.api.domain.geo.base.entity.ColorCode;
import java.util.Optional;

public interface ColorCodeService {

  Optional<ColorCode> findById(final Long id);
}
