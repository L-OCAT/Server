package com.locat.api.infrastructure.redis;

import com.locat.api.domain.auth.entity.LocatRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface LocatRefreshTokenRepository extends CrudRepository<LocatRefreshToken, Long> {}
