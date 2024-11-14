package com.locat.api.infra.persistence.geo.base;

import com.locat.api.domain.geo.base.dto.CategoryInfoDto;
import java.util.List;

public interface CategoryQRepository {

  List<CategoryInfoDto> findAll();
}
