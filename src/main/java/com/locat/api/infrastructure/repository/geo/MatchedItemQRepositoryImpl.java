package com.locat.api.infrastructure.repository.geo;

import com.locat.api.domain.geo.base.entity.QColorCode;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.found.entity.QFoundItem;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.geo.lost.entity.QLostItem;
import com.locat.api.infrastructure.repository.MatchedItemQRepository;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/** 분실물 & 습득물 매칭 QueryDSL Repository */
@Repository
@RequiredArgsConstructor
public class MatchedItemQRepositoryImpl implements MatchedItemQRepository {

  private static final QLostItem qLostItem = QLostItem.lostItem;
  private static final QFoundItem qFoundItem = QFoundItem.foundItem;

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Long countMatchedLostItems(FoundItem foundItem) {
    BooleanExpression isCategoryMatched = qLostItem.category.id.eq(foundItem.getCategoryId());
    BooleanExpression isColorCodeMatched = qLostItem.colorCodes.any().in(foundItem.getColorCodes());
    BooleanExpression locationInRadius =
        Expressions.booleanTemplate(
            "ST_DWithin({0}, {1}, {2})",
            qLostItem.location, foundItem.getLocation(), DEFAULT_MATCH_DISTANCE.getValue());
    return Optional.of(this.jpaQueryFactory)
        .map(
            q ->
                q.select(qLostItem.count())
                    .from(qLostItem)
                    .join(qLostItem.colorCodes, QColorCode.colorCode)
                    .where(isCategoryMatched.and(isColorCodeMatched).and(locationInRadius))
                    .fetchOne())
        .orElse(NONE_MATCHED);
  }

  @Override
  public Long countMatchedFoundItems(LostItem lostItem) {
    BooleanExpression isCategoryMatched = qFoundItem.category.id.eq(lostItem.getCategoryId());
    BooleanExpression isColorCodeMatched = qFoundItem.colorCodes.any().in(lostItem.getColorCodes());
    BooleanExpression locationInRadius =
        Expressions.booleanTemplate(
            "ST_DWithin({0}, {1}, {2})",
            qFoundItem.location, lostItem.getLocation(), DEFAULT_MATCH_DISTANCE.getValue());
    return Optional.of(this.jpaQueryFactory)
        .map(
            q ->
                q.select(qFoundItem.count())
                    .from(qFoundItem)
                    .join(qFoundItem.colorCodes, QColorCode.colorCode)
                    .where(isCategoryMatched.and(isColorCodeMatched).and(locationInRadius))
                    .fetchOne())
        .orElse(NONE_MATCHED);
  }
}
