package com.locat.api.infrastructure.repository.geo.lost.impl;

import com.locat.api.domain.geo.base.dto.GeoItemSearchCriteria;
import com.locat.api.domain.geo.base.utils.GeoUtils;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.geo.lost.entity.QLostItem;
import com.locat.api.infrastructure.repository.geo.AbstractGeoItemQRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
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
        Double.class,
        "ST_Distance_Sphere({0}, {1})",
        qLostItem.location,
        searchCriteria.getLocation());
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
        "CAST(ST_DISTANCE_SPHERE({0}, {1}) AS DOUBLE) <= {2}",
        qLostItem.location, location, GeoUtils.toMeter(distance.getValue()));
  }

  @Override
  protected OrderSpecifier<?> determineOrderSpecification(Sort sort) {
    if (sort.isUnsorted()) {
      return qLostItem.createdAt.desc();
    }
    Sort.Order sortOrder = sort.iterator().next();
    final String sortProperty = sortOrder.getProperty();
    this.assertFieldExists(sortProperty);
    StringPath sortPath =
        new PathBuilder<>(qLostItem.getType(), qLostItem.getMetadata()).getString(sortProperty);
    return sortOrder.isAscending() ? sortPath.asc() : sortPath.desc();
  }

  @Override
  protected boolean areTupleValuesNotNull(Tuple tuple, NumberExpression<Double> distance) {
    return tuple.get(qLostItem) != null && tuple.get(distance) != null;
  }
}
