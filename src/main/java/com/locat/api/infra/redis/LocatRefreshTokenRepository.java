package com.locat.api.infra.redis;

import com.locat.api.domain.auth.entity.LocatRefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface LocatRefreshTokenRepository extends CrudRepository<LocatRefreshToken, Long> {

  Optional<LocatRefreshToken> findByEmail(final String email);
}
