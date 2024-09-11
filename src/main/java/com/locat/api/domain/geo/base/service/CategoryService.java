package com.locat.api.domain.geo.base.service;

import com.locat.api.domain.geo.base.dto.CategoryInfoDto;
import com.locat.api.domain.geo.base.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {

  /**
   * 카테고리 ID로 카테고리 조회
   *
   * @param id 카테고리 ID
   * @return 카테고리
   */
  Optional<Category> findById(final Long id);

  /**
   * 모든 카테고리 조회
   *
   * @return 카테고리 정보 DTO 목록
   */
  List<CategoryInfoDto> findAll();
}
