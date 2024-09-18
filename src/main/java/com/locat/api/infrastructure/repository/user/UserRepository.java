package com.locat.api.infrastructure.repository.user;

import com.locat.api.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByOauthId(String oauthId);

  Boolean existsByEmailHash(String emailHash);

  Boolean existsByNickname(String nickname);
}
