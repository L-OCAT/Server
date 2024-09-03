package com.locat.api.infrastructure.repository.geo.found;

import com.locat.api.domain.geo.found.entity.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {}
