package com.locat.api.infrastructure.repository.lost;

import com.locat.api.domain.geo.base.dto.CategoryInfoDto;
import java.util.List;

public interface CustomCategoryRepository {

  List<CategoryInfoDto> findAll();
}
