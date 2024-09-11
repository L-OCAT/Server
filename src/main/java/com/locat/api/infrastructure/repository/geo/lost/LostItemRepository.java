package com.locat.api.infrastructure.repository.geo.lost;

import com.locat.api.domain.geo.lost.entity.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {}
