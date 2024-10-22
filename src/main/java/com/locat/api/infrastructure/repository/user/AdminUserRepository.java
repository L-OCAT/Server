package com.locat.api.infrastructure.repository.user;

import com.locat.api.domain.user.entity.AdminUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

  Optional<AdminUser> findByEmailHash(String emailHash);
}
