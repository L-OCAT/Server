package com.locat.api.domain.user.service;

import com.locat.api.domain.user.entity.AdminUser;
import java.util.Optional;

public interface AdminUserService {

  Optional<AdminUser> findById(final Long id);

  Optional<AdminUser> findByEmail(final String email);
}
