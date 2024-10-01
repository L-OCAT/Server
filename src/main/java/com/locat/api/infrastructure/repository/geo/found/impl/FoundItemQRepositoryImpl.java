package com.locat.api.infrastructure.repository.geo.found.impl;

import com.locat.api.domain.geo.base.dto.GeoItemSearchCriteria;
import com.locat.api.domain.geo.base.dto.GeoItemSortType;
import com.locat.api.domain.geo.base.utils.GeoUtils;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.found.entity.QFoundItem;
import com.locat.api.infrastructure.repository.geo.AbstractGeoItemQRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.geo.Distance;
import org.springframework.stereotype.Repository;

@Repository
public class FoundItemQRepositoryImpl extends AbstractGeoItemQRepository<FoundItem> {

  private static final QFoundItem qFountItem = QFoundItem.foundItem;

  public FoundItemQRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    super(jpaQueryFactory, qFountItem);
  }

  @Override
  protected NumberExpression<Double> createDistanceExpression(
      GeoItemSearchCriteria searchCriteria) {
    return Expressions.numberTemplate(
        Double.class,
        "ST_Distance_Sphere({0}, {1})",
        qFountItem.location,
        searchCriteria.getLocation());
  }

  @Override
  protected BooleanExpression userIdEquals(Boolean onlyMine, Long userId) {
    if (Boolean.FALSE.equals(onlyMine) || userId == null) {
      return Expressions.TRUE;
    }
    return qFountItem.user.id.eq(userId);
  }

  @Override
  protected BooleanExpression locationInRadius(Point location, Distance distance) {
    if (location == null || distance == null) {
      return null;
    }
    return Expressions.booleanTemplate(
        "CAST(ST_DISTANCE_SPHERE({0}, {1}) AS DOUBLE) <= {2}",
        qFountItem.location, location, GeoUtils.toMeter(distance.getValue()));
  }

  @Override
  protected OrderSpecifier<?> determineOrderSpecification(GeoItemSortType sort) {
    OrderSpecifier<?> orderSpecifier = qFountItem.createdAt.desc();
    if (sort.isFoundAtAsc()) {
      return qFountItem.foundAt.asc();
    }
    if (sort.isFoundAtDesc()) {
      return qFountItem.foundAt.desc();
    }
    if (sort.isCreatedAtAsc()) {
      return qFountItem.createdAt.asc();
    }
    return orderSpecifier;
  }

  @Override
  protected boolean areTupleValuesNotNull(Tuple tuple, NumberExpression<Double> distance) {
    return tuple.get(qFountItem) != null && tuple.get(distance) != null;
  }
}
