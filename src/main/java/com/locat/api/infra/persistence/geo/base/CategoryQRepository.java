package com.locat.api.infra.persistence.geo.base;

import com.locat.api.domain.geo.base.dto.internal.CategoryInfoDto;
import java.util.List;
import java.util.Optional;

public interface CategoryQRepository {

  Optional<CategoryInfoDto> findInfoById(Long id);

  List<CategoryInfoDto> findAll();
}
