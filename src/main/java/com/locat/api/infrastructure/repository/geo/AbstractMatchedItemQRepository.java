package com.locat.api.infrastructure.repository.geo;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractMatchedItemQRepository implements MatchedItemQRepository {

  protected final JPAQueryFactory jpaQueryFactory;

  protected abstract NumberExpression<Double> createDistanceExpression();

  protected abstract BooleanExpression isColorCodeMatched();

  protected abstract BooleanExpression isCategoryMatched();
}
