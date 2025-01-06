package com.locat.api.infra.persistence.geo;

import com.locat.api.domain.geo.base.dto.criteria.GeoItemAdminSearchCriteria;
import com.locat.api.domain.geo.base.dto.internal.AdminGeoItemSearchQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GeoItemAdminQRepository {

  /**
   * 관리자 검색 조건에 따라 {@link AdminGeoItemSearchQueryResult}을 조회합니다.
   *
   * @param searchCriteria 검색 조건
   * @param pageable 페이징 정보
   * @return {@link Page} of {@link AdminGeoItemSearchQueryResult}
   */
  Page<AdminGeoItemSearchQueryResult> findAllByAdminCriteria(
      GeoItemAdminSearchCriteria searchCriteria, final Pageable pageable);
}
