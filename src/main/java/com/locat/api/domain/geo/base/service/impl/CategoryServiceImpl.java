package com.locat.api.domain.geo.base.service.impl;

import com.locat.api.domain.geo.base.dto.CategoryInfoDto;
import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.service.CategoryService;
import com.locat.api.infrastructure.repository.geo.base.CategoryQRepository;
import com.locat.api.infrastructure.repository.geo.base.CategoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryQRepository customCategoryRepository;

  @Override
  @Transactional(readOnly = true)
  public Optional<Category> findById(Long id) {
    return this.categoryRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoryInfoDto> findAll() {
    return this.customCategoryRepository.findAll();
  }
}
