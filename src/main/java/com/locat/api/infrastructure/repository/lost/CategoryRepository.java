package com.locat.api.infrastructure.repository.lost;

import com.locat.api.domain.lost.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
