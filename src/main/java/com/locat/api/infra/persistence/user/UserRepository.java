package com.locat.api.infra.persistence.user;

import com.locat.api.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByOauthId(final String oAuthId);

  Optional<User> findByEmailHash(final String emailHash);

  boolean existsByOauthId(String oAuthId);

  boolean existsByEmailHash(String emailHash);

  boolean existsByNickname(String nickname);
}
