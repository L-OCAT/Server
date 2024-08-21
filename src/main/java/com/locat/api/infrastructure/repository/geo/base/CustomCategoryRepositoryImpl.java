package com.locat.api.infrastructure.repository.geo.base;

import com.locat.api.domain.geo.base.dto.CategoryInfoDto;
import com.locat.api.domain.geo.base.entity.QCategory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {

  private static final QCategory qCategory = QCategory.category;

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  @Transactional(readOnly = true)
  public List<CategoryInfoDto> findAll() {
    QCategory parentCategory = new QCategory("parentCategory");
    return jpaQueryFactory
        .select(
            Projections.constructor(
                CategoryInfoDto.class,
                qCategory.id,
                qCategory.name,
                qCategory.parentId,
                parentCategory.name))
        .from(qCategory)
        .leftJoin(parentCategory)
        .on(qCategory.parentId.eq(parentCategory.id))
        .fetch();
  }
}
