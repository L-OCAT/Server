package com.locat.api.infrastructure.repository.admin.impl;

import com.locat.api.domain.admin.dto.AdminDashboardSummaryDto;
import com.locat.api.domain.admin.dto.AdminItemStatByCategoryDto;
import com.locat.api.domain.admin.dto.AdminMonthlyItemStatDto;
import com.locat.api.domain.admin.dto.inner.MonthlyGeoItemStatistics;
import com.locat.api.domain.geo.base.entity.QCategory;
import com.locat.api.domain.geo.found.entity.QFoundItem;
import com.locat.api.domain.geo.lost.entity.QLostItem;
import com.locat.api.domain.user.entity.QUser;
import com.locat.api.global.utils.DateUtils;
import com.locat.api.infrastructure.repository.admin.AdminDashboardQRepository;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminDashboardQRepositoryImpl implements AdminDashboardQRepository {

  private static final QUser qUser = QUser.user;
  private static final QCategory qCategory = QCategory.category;
  private static final QLostItem qLostItem = QLostItem.lostItem;
  private static final QFoundItem qFoundItem = QFoundItem.foundItem;

  /** 쿼리 조회 시, 날짜 포맷 템플릿 */
  private static final String DATE_FORMAT_TEMPLATE = "DATE_FORMAT({0}, {1})";

  /** {@link MonthlyGeoItemStatistics}의 month 필드 포맷 */
  private static final String MONTH_FORMAT = "%c월";

  /** 월별 통계 조회 기간 / 최근 12개월 */
  private static final int RECENT_MONTHS = 11;

  private final JPAQueryFactory queryFactory;
  private final Clock clock;

  @Override
  public AdminDashboardSummaryDto getSummary() {
    return new AdminDashboardSummaryDto(
        this.countTotalUsers(), this.countTotalLostItems(), this.countTotalFoundItems());
  }

  @Override
  public List<AdminItemStatByCategoryDto> getStatByCategory() {
    QCategory subCategory = new QCategory("subCategory");
    return this.queryFactory
        .select(
            Projections.constructor(
                AdminItemStatByCategoryDto.class,
                qCategory.name,
                qLostItem.countDistinct().coalesce(0L),
                qFoundItem.countDistinct().coalesce(0L)))
        .from(qCategory)
        .leftJoin(subCategory)
        .on(subCategory.parentId.eq(qCategory.id))
        .leftJoin(qFoundItem)
        .on(qFoundItem.category.eq(subCategory))
        .leftJoin(qLostItem)
        .on(qLostItem.category.eq(subCategory))
        .where(qCategory.parentId.isNull())
        .groupBy(qCategory.id, qCategory.name)
        .fetch();
  }

  @Override
  public AdminMonthlyItemStatDto getMonthlyItemStat() {
    LocalDate now = LocalDate.now(this.clock);
    LocalDate startDate = now.minusMonths(RECENT_MONTHS);

    return new AdminMonthlyItemStatDto(
        DateUtils.createMonthLabels(startDate, RECENT_MONTHS),
        this.getMonthlyLostItemCounts(startDate, now),
        this.getMonthlyFoundItemCounts(startDate, now));
  }

  private int countTotalUsers() {
    return Optional.of(this.queryFactory)
        .map(query -> query.select(qUser.count()).from(qUser).fetchOne())
        .map(Math::toIntExact)
        .orElse(0);
  }

  private int countTotalLostItems() {
    return Optional.of(this.queryFactory)
        .map(query -> query.select(qLostItem.count()).from(qLostItem).fetchOne())
        .map(Math::toIntExact)
        .orElse(0);
  }

  private int countTotalFoundItems() {
    return Optional.of(this.queryFactory)
        .map(query -> query.select(qFoundItem.count()).from(qFoundItem).fetchOne())
        .map(Math::toIntExact)
        .orElse(0);
  }

  private List<MonthlyGeoItemStatistics> getMonthlyLostItemCounts(
      LocalDate startDate, LocalDate endDate) {
    return Optional.of(this.queryFactory)
        .map(
            query ->
                query
                    .select(
                        Projections.constructor(
                            MonthlyGeoItemStatistics.class,
                            createMonthTemplate(qLostItem.createdAt),
                            qLostItem.count().intValue()))
                    .from(qLostItem)
                    .where(
                        qLostItem.createdAt.between(
                            startDate.atStartOfDay(), endDate.plusMonths(1).atStartOfDay()))
                    .groupBy(createMonthTemplate(qLostItem.createdAt))
                    .orderBy(createMonthTemplate(qLostItem.createdAt).asc())
                    .fetch())
        .orElse(List.of());
  }

  private List<MonthlyGeoItemStatistics> getMonthlyFoundItemCounts(
      LocalDate startDate, LocalDate endDate) {
    return Optional.of(this.queryFactory)
        .map(
            query ->
                query
                    .select(
                        Projections.constructor(
                            MonthlyGeoItemStatistics.class,
                            createMonthTemplate(qFoundItem.createdAt),
                            qFoundItem.count().intValue()))
                    .from(qFoundItem)
                    .where(
                        qFoundItem.createdAt.between(
                            startDate.atStartOfDay(), endDate.plusMonths(1).atStartOfDay()))
                    .groupBy(createMonthTemplate(qFoundItem.createdAt))
                    .orderBy(createMonthTemplate(qFoundItem.createdAt).asc())
                    .fetch())
        .orElse(List.of());
  }

  private static StringTemplate createMonthTemplate(DateTimePath<LocalDateTime> dateTimePath) {
    return Expressions.stringTemplate(
        DATE_FORMAT_TEMPLATE, dateTimePath, ConstantImpl.create(MONTH_FORMAT));
  }
}
