package com.locat.api.infra.persistence.geo;

import com.locat.api.domain.geo.base.entity.GeoItemAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoItemAddressRepository extends JpaRepository<GeoItemAddress, Long> {}
