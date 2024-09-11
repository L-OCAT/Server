package com.locat.api.infrastructure.repository.geo;

import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.found.entity.QFoundItem;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.geo.lost.entity.QLostItem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MatchedItemQRepositoryImpl implements MatchedItemQRepository {

  private static final QLostItem qLostItem = QLostItem.lostItem;
  private static final QFoundItem qFoundItem = QFoundItem.foundItem;

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Long countMatchedLostItems(FoundItem foundItem) {
    return Optional.of(this.jpaQueryFactory)
        .map(
            q ->
                q.select(qLostItem.count())
                    .from(qLostItem)
                    .where(this.isColorCodeMatched().and(this.isCategoryMatched()))
                    .fetchOne())
        .orElse(NONE_MATCHED);
  }

  @Override
  public Long countMatchedFoundItems(LostItem lostItem) {
    return 0L;
  }

  private BooleanExpression isColorCodeMatched() {
    return qLostItem.colorCodes.any().in(qFoundItem.colorCodes);
  }

  private BooleanExpression isCategoryMatched() {
    return qLostItem.category.eq(qFoundItem.category);
  }
}
