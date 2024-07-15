package com.locat.api.infrastructure.repository.lost;

import com.locat.api.domain.lost.entity.ContactCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactCenterRepository extends JpaRepository<ContactCenter, Long> {
}
