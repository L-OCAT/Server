package com.locat.api.infra.persistence.admin.impl;

import com.locat.api.domain.admin.dto.internal.AdminUserStatDto;
import com.locat.api.domain.geo.found.entity.QFoundItem;
import com.locat.api.domain.geo.lost.entity.QLostItem;
import com.locat.api.domain.terms.entity.QTerms;
import com.locat.api.domain.user.entity.QUser;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.association.QUserTermsAgreement;
import com.locat.api.infra.persistence.admin.UserStatisticQRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserStatisticQRepositoryImpl implements UserStatisticQRepository {

  private static final QUser qUser = QUser.user;
  private static final QTerms qTerms = QTerms.terms;
  private static final QUserTermsAgreement qUserTermsAgreement =
      QUserTermsAgreement.userTermsAgreement;
  private static final QLostItem qLostItem = QLostItem.lostItem;
  private static final QFoundItem qFoundItem = QFoundItem.foundItem;

  private final JPAQueryFactory queryFactory;

  @Override
  public AdminUserStatDto getUserStat(User user) {
    List<AdminUserStatDto.AgreementDetail> agreementDetail = this.fetchAgreementDetail(user);
    AdminUserStatDto adminUserStatDto =
        this.queryFactory
            .select(
                Projections.constructor(
                    AdminUserStatDto.class,
                    qUser.id,
                    qUser.userType,
                    qUser.oauthType,
                    qUser.email,
                    qUser.nickname,
                    qUser.statusType,
                    qUser.createdAt,
                    qUser.updatedAt,
                    qUser.deletedAt,
                    Expressions.constant(Collections.emptyList()),
                    Projections.constructor(
                        AdminUserStatDto.ActivityDetails.class,
                        JPAExpressions.select(qLostItem.count().intValue())
                            .from(qLostItem)
                            .where(qLostItem.user.eq(qUser)),
                        JPAExpressions.select(qFoundItem.count().intValue())
                            .from(qFoundItem)
                            .where(qFoundItem.user.eq(qUser)))))
            .from(qUser)
            .leftJoin(qUserTermsAgreement)
            .on(qUserTermsAgreement.user.eq(qUser))
            .where(qUser.eq(user))
            .groupBy(qUser.id)
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

  private List<AdminUserStatDto.AgreementDetail> fetchAgreementDetail(User user) {
    return this.queryFactory
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
