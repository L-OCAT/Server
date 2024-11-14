package com.locat.api.infra.persistence.geo;

import com.locat.api.domain.geo.base.dto.GeoItemSearchCriteria;
import com.locat.api.domain.geo.base.entity.GeoItem;
import com.locat.api.global.exception.custom.InvalidParameterException;
import com.locat.api.global.utils.ValidationUtils;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.*;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public abstract class AbstractGeoItemQRepository<T extends GeoItem>
    implements GeoItemQRepository<T> {

  /** QueryDSL Factory */
  protected final JPAQueryFactory jpaQueryFactory;

  /** QueryDSL Entity */
  protected final EntityPathBase<T> qEntity;

  /**
   * {@inheritDoc}
   *
   * @param userId 사용자 ID
   * @param searchCriteria 검색 조건
   * @param pageable 페이징 정보
   * @return {@link GeoPage}로 래핑된 검색 결과
   */
  @Transactional(readOnly = true)
  public GeoPage<T> findByCondition(
      final Long userId, GeoItemSearchCriteria searchCriteria, final Pageable pageable) {
    NumberExpression<Double> distance = this.createDistanceExpression(searchCriteria);
    GeoResults<T> geoResults =
        new GeoResults<>(
            this.jpaQueryFactory
                .select(this.qEntity, distance)
                .from(this.qEntity)
                .where(
                    this.userIdEquals(searchCriteria.getOnlyMine(), userId),
                    this.locationInRadius(
                        searchCriteria.getLocation(), searchCriteria.getDistance()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(distance.asc(), this.determineOrderSpecification(pageable.getSort()))
                .fetch()
                .stream()
                .filter(tuple -> this.areTupleValuesNotNull(tuple, distance))
                .map(
                    tuple ->
                        new GeoResult<>(tuple.get(this.qEntity), new Distance(tuple.get(distance))))
                .toList(),
            searchCriteria.getDistance());
    final long totalCount =
        Optional.of(this.jpaQueryFactory)
            .map(
                q ->
                    q.select(this.qEntity.count())
                        .from(this.qEntity)
                        .where(
                            this.userIdEquals(searchCriteria.getOnlyMine(), userId),
                            this.locationInRadius(
                                searchCriteria.getLocation(), searchCriteria.getDistance()))
                        .fetchOne())
            .orElse(0L);
    return new GeoPage<>(geoResults, pageable, totalCount);
  }

  protected void assertFieldExists(String fieldName) {
    ValidationUtils.throwIf(
        fieldName,
        value ->
            Arrays.stream(this.qEntity.getType().getDeclaredFields())
                .noneMatch(field -> field.getName().equals(value)),
        () -> new InvalidParameterException(String.format("Field %s does not exist", fieldName)));
  }

  /**
   * 지정된 검색 조건에 따른 좌표 간 거리 계산을 위한 표현식을 반환합니다.
   *
   * @param searchCriteria 검색 조건
   * @return 두 좌표 간의 거리 계산을 위한 NumberExpression
   */
  protected abstract NumberExpression<Double> createDistanceExpression(
      GeoItemSearchCriteria searchCriteria);

  /**
   * 주어진 사용자 ID와 일치하는지 여부 조건을 반환합니다.
   *
   * @param onlyMine 내가 등록한 항목만 조회할지 여부
   * @param userId 사용자 ID
   * @return 사용자 ID와 일치하는 조건을 나타내는 BooleanExpression
   */
  protected abstract BooleanExpression userIdEquals(Boolean onlyMine, Long userId);

  /**
   * 주어진 위치와 거리 내에 포함되는 좌표를 나타내지 여부 조건을 반환합니다.
   *
   * @param location 기준이 되는 위치 (Point 객체)
   * @param distance 기준 위치로부터의 거리
   * @return 주어진 거리 내의 위치를 나타내는 조건을 나타내는 BooleanExpression
   */
  protected abstract BooleanExpression locationInRadius(Point location, Distance distance);

  /**
   * 지정된 정렬 유형에 따라 정렬 기준을 반환합니다.
   *
   * @param sort 정렬 기준을 나타내는 Sort 객체
   * @return 주어진 정렬 기준에 따라 생성된 OrderSpecifier 객체
   */
  protected abstract OrderSpecifier<?> determineOrderSpecification(Sort sort);

  /**
   * Tuple에 포함된 값들이 null이 아닌지 확인합니다.
   *
   * @param tuple 검증할 Tuple 객체
   * @param distance 거리 계산에 사용된 NumberExpression
   * @return Tuple의 값들이 null이 아닌 경우 true, 그렇지 않으면 false
   */
  protected abstract boolean areTupleValuesNotNull(Tuple tuple, NumberExpression<Double> distance);
}
