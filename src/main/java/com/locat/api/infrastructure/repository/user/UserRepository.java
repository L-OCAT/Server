package com.locat.api.infrastructure.repository.user;

import com.locat.api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Boolean existsByEmailHash(String emailHash);

  Boolean existsByNickname(String nickname);
}
