package com.locat.api.domain.geo.base.service;

import com.locat.api.domain.geo.base.dto.criteria.GeoItemAdminSearchCriteria;
import com.locat.api.domain.geo.base.dto.internal.AdminGeoItemSearchDto;
import com.locat.api.domain.geo.base.entity.GeoItemAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GeoItemAddressService {

  void save(GeoItemAddress geoItemAddress);

  Page<AdminGeoItemSearchDto> findAllByAdminCriteria(
      GeoItemAdminSearchCriteria searchCriteria, Pageable pageable);
}
