package com.locat.api.infra.persistence.geo.base;

import com.locat.api.domain.geo.base.entity.ColorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorCodeRepository extends JpaRepository<ColorCode, Long> {}
