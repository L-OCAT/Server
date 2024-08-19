package com.locat.api.domain.user.service;

import com.locat.api.domain.user.entity.User;
import java.util.Optional;

public interface UserService {

  User save(final User user);

  Optional<User> findByEmail(final String email);

  Optional<User> findByOauthId(final String oauthId);
}
