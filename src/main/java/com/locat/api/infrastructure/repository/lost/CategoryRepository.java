package com.locat.api.infrastructure.repository.lost;

import com.locat.api.domain.geo.base.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
