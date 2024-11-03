package com.locat.api.infrastructure.repository.user.impl;

import com.locat.api.domain.user.dto.AdminUserSearchCriteria;
import com.locat.api.domain.user.dto.UserInfoDto;
import com.locat.api.domain.user.entity.QUser;
import com.locat.api.global.utils.HashingUtils;
import com.locat.api.global.utils.QueryUtils;
import com.locat.api.infrastructure.repository.user.UserQRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class UserQRepositoryImpl implements UserQRepository {

  private static final QUser qUser = QUser.user;

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<UserInfoDto> findAllByCriteria(AdminUserSearchCriteria criteria, Pageable pageable) {
    List<UserInfoDto> contents =
        this.queryFactory
            .select(
                Projections.constructor(
                    UserInfoDto.class,
                    qUser.id,
                    qUser.userType,
                    qUser.oauthType,
                    qUser.email,
                    qUser.nickname,
                    qUser.statusType,
                    qUser.createdAt,
                    qUser.updatedAt,
                    qUser.deletedAt))
            .from(qUser)
            .where(
                this.nicknameLike(criteria.nickname()),
                this.emailEquals(criteria.email()),
                this.createdAtBetween(criteria))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    final long totalCount =
        Optional.of(this.queryFactory)
            .map(
                query ->
                    query
                        .select(qUser.count())
                        .from(qUser)
                        .where(
                            this.nicknameLike(criteria.nickname()),
                            this.emailEquals(criteria.email()),
                            this.createdAtBetween(criteria))
                        .fetchOne())
            .orElse(0L);

    return QueryUtils.toPage(contents, pageable, totalCount);
  }

  private BooleanExpression nicknameLike(String nickname) {
    return StringUtils.hasText(nickname) ? qUser.nickname.contains(nickname) : null;
  }

  private BooleanExpression emailEquals(String email) {
    return StringUtils.hasText(email) ? qUser.emailHash.eq(HashingUtils.hash(email)) : null;
  }

  private BooleanExpression createdAtBetween(AdminUserSearchCriteria criteria) {
    LocalDate startDate = criteria.startDate();
    LocalDate endDate = criteria.endDate();
    if (startDate == null || endDate == null) {
      return null;
    }
    return qUser.createdAt.between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
  }
}
