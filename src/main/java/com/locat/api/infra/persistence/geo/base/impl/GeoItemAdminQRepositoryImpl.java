package com.locat.api.infra.persistence.geo.base.impl;

import com.locat.api.domain.geo.base.dto.criteria.GeoItemAdminSearchCriteria;
import com.locat.api.domain.geo.base.dto.internal.AdminGeoItemSearchQueryResult;
import com.locat.api.domain.geo.base.entity.GeoItemType;
import com.locat.api.domain.geo.base.entity.QCategory;
import com.locat.api.domain.geo.base.entity.QGeoItemAddress;
import com.locat.api.domain.geo.found.entity.QFoundItem;
import com.locat.api.domain.geo.lost.entity.QLostItem;
import com.locat.api.global.utils.QueryUtils;
import com.locat.api.infra.persistence.geo.GeoItemAdminQRepository;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.jpa.impl.AbstractJPAQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class GeoItemAdminQRepositoryImpl implements GeoItemAdminQRepository {

  private static final QLostItem qLostItem = QLostItem.lostItem;
  private static final QCategory qLostItemCategory = QCategory.category;
  private static final QFoundItem qFoundItem = QFoundItem.foundItem;
  private static final QCategory qFoundItemCategory = QCategory.category;
  private static final QGeoItemAddress qGeoItemAddress = QGeoItemAddress.geoItemAddress;

  private final QueryBuilder queryBuilder;

  public GeoItemAdminQRepositoryImpl(JPAQueryFactory queryFactory) {
    this.queryBuilder = new QueryBuilder(queryFactory);
  }

  @Override
  public Page<AdminGeoItemSearchQueryResult> findAllByAdminCriteria(
      GeoItemAdminSearchCriteria searchCriteria, Pageable pageable) {
    List<AdminGeoItemSearchQueryResult> queryResult =
        this.queryBuilder
            .createSearchQuery(searchCriteria)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    final long totalCount =
        Optional.of(this.queryBuilder.createCountQuery(searchCriteria))
            .map(AbstractJPAQuery::fetchOne)
            .orElse(0L);

    return QueryUtils.toPage(queryResult, pageable, totalCount);
  }

  @RequiredArgsConstructor
  static class QueryBuilder {

    private final JPAQueryFactory queryFactory;

    public JPAQuery<AdminGeoItemSearchQueryResult> createSearchQuery(
        GeoItemAdminSearchCriteria searchCriteria) {
      GeoItemType geoItemType = searchCriteria.itemType();
      return this.queryFactory
          .select(
              Projections.constructor(
                  AdminGeoItemSearchQueryResult.class,
                  selectByType(qLostItem.id, qFoundItem.id),
                  qGeoItemAddress.itemType,
                  selectByType(qLostItem.name, qFoundItem.name),
                  selectByType(qLostItem.category.id, qFoundItem.category.id),
                  selectByType(qLostItem.createdAt, qFoundItem.createdAt),
                  qGeoItemAddress.latitude,
                  qGeoItemAddress.longitude,
                  qGeoItemAddress.region1,
                  qGeoItemAddress.region2,
                  qGeoItemAddress.region3,
                  qGeoItemAddress.roadAddress,
                  qGeoItemAddress.buildingName))
          .from(qGeoItemAddress)
          .leftJoin(qLostItem)
          .on(
              qGeoItemAddress
                  .itemId
                  .eq(qLostItem.id)
                  .and(qGeoItemAddress.itemType.eq(GeoItemType.LOST)))
          .leftJoin(qFoundItem)
          .on(
              qGeoItemAddress
                  .itemId
                  .eq(qFoundItem.id)
                  .and(qGeoItemAddress.itemType.eq(GeoItemType.FOUND)))
          .leftJoin(qLostItemCategory)
          .on(qLostItem.category.id.eq(qLostItemCategory.id))
          .leftJoin(qFoundItemCategory)
          .on(qFoundItem.category.id.eq(qFoundItemCategory.id))
          .where(
              this.geoItemTypeEquals(geoItemType),
              this.itemNameLike(geoItemType, searchCriteria.itemName()),
              this.categoryEquals(geoItemType, searchCriteria.categoryId()),
              this.regionEquals(searchCriteria),
              this.createdAtBetween(geoItemType, searchCriteria.from(), searchCriteria.to()));
    }

    public JPAQuery<Long> createCountQuery(GeoItemAdminSearchCriteria searchCriteria) {
      GeoItemType geoItemType = searchCriteria.itemType();
      return this.queryFactory
          .select(qGeoItemAddress.count())
          .from(qGeoItemAddress)
          .leftJoin(qLostItem)
          .on(
              qGeoItemAddress
                  .itemId
                  .eq(qLostItem.id)
                  .and(qGeoItemAddress.itemType.eq(GeoItemType.LOST)))
          .leftJoin(qFoundItem)
          .on(
              qGeoItemAddress
                  .itemId
                  .eq(qFoundItem.id)
                  .and(qGeoItemAddress.itemType.eq(GeoItemType.FOUND)))
          .leftJoin(qLostItemCategory)
          .on(qLostItem.category.id.eq(qLostItemCategory.id))
          .leftJoin(qFoundItemCategory)
          .on(qFoundItem.category.id.eq(qFoundItemCategory.id))
          .where(
              this.geoItemTypeEquals(geoItemType),
              this.itemNameLike(geoItemType, searchCriteria.itemName()),
              this.categoryEquals(geoItemType, searchCriteria.categoryId()),
              this.regionEquals(searchCriteria),
              this.createdAtBetween(geoItemType, searchCriteria.from(), searchCriteria.to()));
    }

    private static <T> SimpleExpression<T> selectByType(
        Expression<T> lostExpression, Expression<T> foundExpression) {
      return Expressions.cases()
          .when(qGeoItemAddress.itemType.eq(GeoItemType.LOST))
          .then(lostExpression)
          .otherwise(foundExpression);
    }

    private BooleanExpression geoItemTypeEquals(GeoItemType geoItemType) {
      return geoItemType != null ? qGeoItemAddress.itemType.eq(geoItemType) : null;
    }

    private BooleanExpression itemNameLike(GeoItemType geoItemType, String itemName) {
      if (!StringUtils.hasText(itemName)) {
        return null;
      }
      if (geoItemType != null) {
        return geoItemType == GeoItemType.LOST
            ? qLostItem.name.contains(itemName)
            : qFoundItem.name.contains(itemName);
      }

      // 전체 검색인 경우
      BooleanExpression lostCondition =
          qLostItem.name.contains(itemName).and(qGeoItemAddress.itemType.eq(GeoItemType.LOST));
      BooleanExpression foundCondition =
          qFoundItem.name.contains(itemName).and(qGeoItemAddress.itemType.eq(GeoItemType.FOUND));

      return lostCondition.or(foundCondition);
    }

    private BooleanExpression categoryEquals(GeoItemType geoItemType, Long categoryId) {
      if (categoryId == null) {
        return null;
      }

      if (geoItemType != null) {
        if (geoItemType == GeoItemType.LOST) {
          return qLostItemCategory.id.eq(categoryId).or(qLostItemCategory.parentId.eq(categoryId));
        } else {
          return qFoundItemCategory
              .id
              .eq(categoryId)
              .or(qFoundItemCategory.parentId.eq(categoryId));
        }
      }

      // 전체 검색인 경우
      BooleanExpression lostCondition =
          (qLostItemCategory.id.eq(categoryId).or(qLostItemCategory.parentId.eq(categoryId)))
              .and(qGeoItemAddress.itemType.eq(GeoItemType.LOST));

      BooleanExpression foundCondition =
          (qFoundItemCategory.id.eq(categoryId).or(qFoundItemCategory.parentId.eq(categoryId)))
              .and(qGeoItemAddress.itemType.eq(GeoItemType.FOUND));

      return lostCondition.or(foundCondition);
    }

    private BooleanExpression regionEquals(GeoItemAdminSearchCriteria searchCriteria) {
      String region1 = searchCriteria.region1();
      String region2 = searchCriteria.region2();
      String region3 = searchCriteria.region3();

      if (region3 != null) {
        return qGeoItemAddress
            .region3
            .eq(region3)
            .and(qGeoItemAddress.region2.eq(region2))
            .and(qGeoItemAddress.region1.eq(region1));
      }

      if (region2 != null) {
        return qGeoItemAddress.region2.eq(region2).and(qGeoItemAddress.region1.eq(region1));
      }

      if (region1 != null) {
        return qGeoItemAddress.region1.eq(region1);
      }

      return null;
    }

    private BooleanExpression createdAtBetween(
        GeoItemType geoItemType, LocalDate from, LocalDate to) {
      if (from == null || to == null) {
        return null;
      }
      LocalDateTime fromDateTime = from.atStartOfDay();
      LocalDateTime toDateTime = to.atTime(LocalTime.MAX);

      // 일부 타입만 검색하는 경우
      if (geoItemType != null) {
        return geoItemType == GeoItemType.LOST
            ? qLostItem.createdAt.between(fromDateTime, toDateTime)
            : qFoundItem.createdAt.between(fromDateTime, toDateTime);
      }

      // 전체 검색인 경우
      BooleanExpression lostCondition =
          qLostItem
              .createdAt
              .between(fromDateTime, toDateTime)
              .and(qGeoItemAddress.itemType.eq(GeoItemType.LOST));
      BooleanExpression foundCondition =
          qFoundItem
              .createdAt
              .between(fromDateTime, toDateTime)
              .and(qGeoItemAddress.itemType.eq(GeoItemType.FOUND));

      return lostCondition.or(foundCondition);
    }
  }
}
