package com.locat.api.domain.geo.base.service;

import com.locat.api.domain.geo.base.dto.CategoryInfoDto;
import com.locat.api.infrastructure.repository.lost.CustomCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CustomCategoryRepository customCategoryRepository;

  @Override
  @Transactional(readOnly = true)
  public List<CategoryInfoDto> findAll() {
    return this.customCategoryRepository.findAll();
  }
}
