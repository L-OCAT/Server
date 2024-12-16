package com.locat.api.domain.geo.base.service;

import com.locat.api.domain.geo.base.dto.criteria.GeoItemAdminSearchCriteria;
import com.locat.api.domain.geo.base.dto.internal.AdminGeoItemSearchDto;
import com.locat.api.domain.geo.base.dto.response.GeoItemDetailResponse;
import com.locat.api.domain.geo.base.entity.GeoItemAddress;
import com.locat.api.global.exception.custom.InternalProcessingException;
import com.locat.api.global.exception.custom.InvalidParameterException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GeoItemAddressService {

  /**
   * 분실/습득물 주소 정보를 저장합니다.
   *
   * @param geoItemAddress 분실/습득물 주소 정보
   */
  void save(GeoItemAddress geoItemAddress);

  /**
   * 관리자 페이지용 분실/습득물 주소 정보를 검색합니다.
   *
   * @param searchCriteria 검색 조건
   * @param pageable 페이징 정보
   * @return 검색 결과
   * @throws InvalidParameterException 검색 조건이 잘못된 경우
   * @throws InternalProcessingException 처리 중 문제가 발생한 경우
   */
  Page<AdminGeoItemSearchDto> findAllByAdminCriteria(
      GeoItemAdminSearchCriteria searchCriteria, Pageable pageable);

  GeoItemDetailResponse getGeoItemDetail(Long id);
}
