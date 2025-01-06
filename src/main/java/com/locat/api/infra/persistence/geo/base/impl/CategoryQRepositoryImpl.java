package com.locat.api.infra.persistence.geo.base.impl;

import com.locat.api.domain.geo.base.dto.internal.CategoryInfoDto;
import com.locat.api.domain.geo.base.entity.QCategory;
import com.locat.api.infra.persistence.geo.base.CategoryQRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class CategoryQRepositoryImpl implements CategoryQRepository {

  private static final QCategory qCategory = QCategory.category;

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<CategoryInfoDto> findInfoById(Long id) {
    QCategory parentCategory = new QCategory("parentCategory");
    return Optional.of(this.queryFactory)
        .map(
            query ->
                query
                    .select(
                        Projections.constructor(
                            CategoryInfoDto.class,
                            qCategory.id,
                            qCategory.name,
                            parentCategory.id,
                            parentCategory.name))
                    .from(qCategory)
                    .leftJoin(parentCategory)
                    .on(qCategory.parentId.eq(parentCategory.id))
                    .where(qCategory.id.eq(id))
                    .fetchOne());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoryInfoDto> findAll() {
    QCategory parentCategory = new QCategory("parentCategory");
    return this.queryFactory
        .select(
            Projections.constructor(
                CategoryInfoDto.class,
                qCategory.id,
                qCategory.name,
                parentCategory.id,
                parentCategory.name))
        .from(qCategory)
        .leftJoin(parentCategory)
        .on(qCategory.parentId.eq(parentCategory.id))
        .fetch();
  }
}
