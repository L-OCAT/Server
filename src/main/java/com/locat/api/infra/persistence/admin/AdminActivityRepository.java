package com.locat.api.infra.persistence.admin;

import com.locat.api.domain.admin.entity.AdminActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminActivityRepository extends JpaRepository<AdminActivity, Long> {}
