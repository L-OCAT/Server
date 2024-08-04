package com.locat.api.infrastructure.repository.user;

import com.locat.api.domain.user.entity.UserOAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOAuthRepository extends JpaRepository<UserOAuth, Long> {

  Optional<UserOAuth> findByOauthId(String oauthId);
}
