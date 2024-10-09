package com.locat.api.infrastructure.repository.terms.impl;

import com.locat.api.domain.terms.entity.QTerms;
import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.infrastructure.repository.terms.TermsQRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TermsQRepositoryImpl implements TermsQRepository {

  private static final QTerms qTerms = QTerms.terms;

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Optional<Terms> findLatestByType(TermsType termsType) {
    return Optional.of(this.jpaQueryFactory)
        .map(
            qFactory ->
                qFactory
                    .selectFrom(qTerms)
                    .where(qTerms.type.eq(termsType))
                    .orderBy(qTerms.version.desc())
                    .fetchFirst());
  }

  @Override
  public List<Terms> findAllLatest() {
    QTerms subTerms = new QTerms("subTerms");
    return this.jpaQueryFactory
        .selectFrom(qTerms)
        .where(
            qTerms.version.eq(
                this.jpaQueryFactory
                    .select(subTerms.version.max())
                    .from(subTerms)
                    .where(subTerms.type.eq(qTerms.type))))
        .fetch();
  }
}
