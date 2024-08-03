package com.locat.api.infrastructure.redis;

import com.locat.api.domain.auth.entity.LocatAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface LocatAccessTokenRepository extends CrudRepository<LocatAccessToken, Long> {}
