package com.locat.api.infrastructure.repository.geo.base;

import com.locat.api.domain.geo.base.entity.ContactCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactCenterRepository extends JpaRepository<ContactCenter, Long> {}
