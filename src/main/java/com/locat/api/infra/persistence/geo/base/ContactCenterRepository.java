package com.locat.api.infra.persistence.geo.base;

import com.locat.api.domain.geo.base.entity.ContactCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactCenterRepository extends JpaRepository<ContactCenter, Long> {}
