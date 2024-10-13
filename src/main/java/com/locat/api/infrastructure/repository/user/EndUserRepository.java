package com.locat.api.infrastructure.repository.user;

import com.locat.api.domain.user.entity.EndUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndUserRepository extends JpaRepository<EndUser, Long> {

  Optional<EndUser> findByOauthId(String oauthId);

  boolean existsByOauthId(String oauthId);

  boolean existsByEmailHash(String emailHash);

  boolean existsByNickname(String nickname);
}
