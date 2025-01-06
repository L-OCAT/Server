package com.locat.api.infra.persistence.terms.impl;

import com.locat.api.domain.terms.dto.internal.TermsRevisionCompactHistoryDto;
import com.locat.api.domain.terms.entity.QTermsRevisionHistory;
import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.infra.persistence.terms.TermsRevisionHistoryQRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TermsRevisionHistoryQRepositoryImpl implements TermsRevisionHistoryQRepository {

  private static final QTermsRevisionHistory qTermsRevisionHistory =
      QTermsRevisionHistory.termsRevisionHistory;

  private final JPAQueryFactory queryFactory;

  @Override
  public List<TermsRevisionCompactHistoryDto> findCompactHistoriesByType(TermsType termsType) {
    return this.queryFactory
        .select(
            Projections.constructor(
                TermsRevisionCompactHistoryDto.class,
                qTermsRevisionHistory.version,
                qTermsRevisionHistory.revisionNote,
                qTermsRevisionHistory.createdAt))
        .from(qTermsRevisionHistory)
        .where(qTermsRevisionHistory.type.eq(termsType))
        .fetch();
  }
}
