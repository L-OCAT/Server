package com.locat.api.infra.persistence.geo;

import com.locat.api.domain.geo.base.dto.GeoItemSearchCriteria;
import com.locat.api.domain.geo.base.entity.GeoItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoPage;

/**
 * 분실물 & 습득물 QueryDSL Repository
 *
 * @param <T> Entity ({@link GeoItem}의 하위 타입)
 */
public interface GeoItemQRepository<T extends GeoItem> {

  /**
   * 검색 조건에 따라 {@link GeoItem}을 조회합니다.
   *
   * @param userId 사용자 ID
   * @param searchCriteria 검색 조건
   * @param pageable 페이징 정보
   * @return {@link GeoPage}
   */
  GeoPage<T> findByCondition(
      final Long userId, GeoItemSearchCriteria searchCriteria, final Pageable pageable);
}
