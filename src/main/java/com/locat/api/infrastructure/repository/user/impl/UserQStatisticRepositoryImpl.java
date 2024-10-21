package com.locat.api.infrastructure.repository.user.impl;

import com.locat.api.domain.admin.dto.AdminUserStatDto;
import com.locat.api.domain.geo.found.entity.QFoundItem;
import com.locat.api.domain.geo.lost.entity.QLostItem;
import com.locat.api.domain.terms.entity.QTerms;
import com.locat.api.domain.user.entity.EndUser;
import com.locat.api.domain.user.entity.QEndUser;
import com.locat.api.domain.user.entity.association.QUserTermsAgreement;
import com.locat.api.infrastructure.repository.user.UserQStatisticRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQStatisticRepositoryImpl implements UserQStatisticRepository {

  private static final QEndUser qEndUser = QEndUser.endUser;
  private static final QTerms qTerms = QTerms.terms;
  private static final QUserTermsAgreement qUserTermsAgreement =
      QUserTermsAgreement.userTermsAgreement;
  private static final QLostItem qLostItem = QLostItem.lostItem;
  private static final QFoundItem qFoundItem = QFoundItem.foundItem;

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public AdminUserStatDto getUserStat(EndUser user) {
    return jpaQueryFactory
        .select(
            Projections.constructor(
                AdminUserStatDto.class,
                qEndUser.id,
                qEndUser.userType,
                qEndUser.oauthType,
                qEndUser.email,
                qEndUser.nickname,
                qEndUser.statusType,
                qEndUser.createdAt,
                qEndUser.updatedAt,
                qEndUser.deletedAt,
                Projections.constructor(
                    AdminUserStatDto.ActivityDetails.class,
                    qLostItem.count().intValue(),
                    qFoundItem.count().intValue())))
        .from(qEndUser)
        .leftJoin(qLostItem)
        .on(qLostItem.user.eq(qEndUser))
        .leftJoin(qFoundItem)
        .on(qFoundItem.user.eq(qEndUser))
        .where(qEndUser.eq(user.asEndUser()))
        .groupBy(qEndUser.id)
        .fetchOne();
  }
}
