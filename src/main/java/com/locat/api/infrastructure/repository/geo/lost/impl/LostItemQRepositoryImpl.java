package com.locat.api.infrastructure.repository.geo.lost.impl;

import com.locat.api.domain.geo.base.dto.GeoItemSearchCriteria;
import com.locat.api.domain.geo.base.dto.GeoItemSortType;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.geo.lost.entity.QLostItem;
import com.locat.api.infrastructure.repository.geo.AbstractGeoItemQRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.geo.*;
import org.springframework.stereotype.Repository;

@Repository
public class LostItemQRepositoryImpl extends AbstractGeoItemQRepository<LostItem> {

  private static final QLostItem qLostItem = QLostItem.lostItem;

  public LostItemQRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    super(jpaQueryFactory, qLostItem);
  }

  @Override
  protected NumberExpression<Double> createDistanceExpression(
      GeoItemSearchCriteria searchCriteria) {
    return Expressions.numberTemplate(
        Double.class, "ST_Distance({0}, {1})", qLostItem.location, searchCriteria.getLocation());
  }

  @Override
  protected BooleanExpression userIdEquals(Boolean onlyMine, Long userId) {
    if (Boolean.FALSE.equals(onlyMine) || userId == null) {
      return null;
    }
    return qLostItem.user.id.eq(userId);
  }

  @Override
  protected BooleanExpression locationInRadius(Point location, Distance distance) {
    if (location == null || distance == null) {
      return null;
    }
    return Expressions.booleanTemplate(
        "ST_DWithin({0}, {1}, {2}) = true", qLostItem.location, location, distance.getValue());
  }

  @Override
  protected OrderSpecifier<?> determineOrderSpecification(GeoItemSortType sort) {
    OrderSpecifier<?> orderSpecifier = qLostItem.createdAt.desc();
    if (sort.isLostAtDesc()) {
      return qLostItem.lostAt.desc();
    }
    if (sort.isLostAtAsc()) {
      return qLostItem.lostAt.asc();
    }
    if (sort.isCreatedAtDesc()) {
      return qLostItem.createdAt.desc();
    }
    return orderSpecifier;
  }

  @Override
  protected boolean areTupleValuesNotNull(Tuple tuple, NumberExpression<Double> distance) {
    return tuple.get(qLostItem) != null && tuple.get(distance) != null;
  }
}
