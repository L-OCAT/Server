package com.locat.api.infrastructure.repository.geo.base;

import com.locat.api.domain.geo.base.dto.CategoryInfoDto;
import java.util.List;

public interface CustomCategoryRepository {

  List<CategoryInfoDto> findAll();
}
