package com.locat.api.infra.persistence.geo.found.impl;

import com.locat.api.domain.geo.base.dto.criteria.GeoItemSearchCriteria;
import com.locat.api.domain.geo.base.utils.GeoUtils;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.found.entity.QFoundItem;
import com.locat.api.infra.persistence.geo.AbstractGeoItemQRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Sort;
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
  protected OrderSpecifier<?> determineOrderSpecification(Sort sort) {
    if (sort.isUnsorted()) {
      return qFountItem.createdAt.desc();
    }
    Sort.Order sortOrder = sort.iterator().next();
    final String sortProperty = sortOrder.getProperty();
    this.assertFieldExists(sortProperty);

    StringPath path =
        new PathBuilder<>(qFountItem.getType(), qFountItem.getMetadata()).getString(sortProperty);
    return sortOrder.isAscending() ? path.asc() : path.desc();
  }

  @Override
  protected boolean areTupleValuesNotNull(Tuple tuple, NumberExpression<Double> distance) {
    return tuple.get(qFountItem) != null && tuple.get(distance) != null;
  }
}
