package com.locat.api.infrastructure.redis;

import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface OAuth2ProviderTokenRepository extends CrudRepository<OAuth2ProviderToken, String> {

  Optional<OAuth2ProviderToken> findByAccessToken(final String accessToken);
}
