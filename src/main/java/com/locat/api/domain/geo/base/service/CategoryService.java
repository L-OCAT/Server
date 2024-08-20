package com.locat.api.domain.geo.base.service;

import com.locat.api.domain.geo.base.dto.CategoryInfoDto;
import java.util.List;

public interface CategoryService {

  List<CategoryInfoDto> findAll();
}
