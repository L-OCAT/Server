package com.locat.api.domain.geo.base.service.impl;

import com.locat.api.domain.geo.base.dto.internal.CategoryInfoDto;
import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.service.CategoryService;
import com.locat.api.infra.persistence.geo.base.CategoryQRepository;
import com.locat.api.infra.persistence.geo.base.CategoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryQRepository categoryQRepository;

  @Override
  public Optional<Category> findById(Long id) {
    return this.categoryRepository.findById(id);
  }

  @Override
  @Cacheable(value = "CATEGORYINFO", key = "#id")
  public Optional<CategoryInfoDto> findInfoById(Long id) {
    return this.categoryQRepository.findInfoById(id);
  }

  @Override
  public List<CategoryInfoDto> findAll() {
    return this.categoryQRepository.findAll();
  }
}
