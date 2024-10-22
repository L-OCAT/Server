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
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
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
    List<AdminUserStatDto.AgreementDetail> agreementDetail = this.fetchAgreementDetail(user);
    AdminUserStatDto adminUserStatDto =
        this.jpaQueryFactory
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
                    Expressions.constant(Collections.emptyList()),
                    Projections.constructor(
                        AdminUserStatDto.ActivityDetails.class,
                        qLostItem.count().intValue(),
                        qFoundItem.count().intValue())))
            .from(qEndUser)
            .leftJoin(qLostItem)
            .on(qLostItem.user.eq(qEndUser))
            .leftJoin(qFoundItem)
            .on(qFoundItem.user.eq(qEndUser))
            .leftJoin(qUserTermsAgreement)
            .on(qUserTermsAgreement.user.eq(qEndUser))
            .where(qEndUser.eq(user))
            .groupBy(qEndUser.id)
            .fetchOne();
    return new AdminUserStatDto(
        adminUserStatDto.id(),
        adminUserStatDto.type(),
        adminUserStatDto.oAuthType(),
        adminUserStatDto.email(),
        adminUserStatDto.nickname(),
        adminUserStatDto.statusType(),
        adminUserStatDto.createdAt(),
        adminUserStatDto.updatedAt(),
        adminUserStatDto.deletedAt(),
        agreementDetail,
        adminUserStatDto.activityDetails());
  }

  private List<AdminUserStatDto.AgreementDetail> fetchAgreementDetail(EndUser user) {
    return this.jpaQueryFactory
        .select(
            Projections.constructor(
                AdminUserStatDto.AgreementDetail.class,
                qTerms.type,
                qUserTermsAgreement.isNotNull(),
                qUserTermsAgreement.createdAt))
        .from(qTerms)
        .leftJoin(qUserTermsAgreement)
        .on(qUserTermsAgreement.terms.eq(qTerms).and(qUserTermsAgreement.user.eq(user)))
        .fetch();
  }
}
