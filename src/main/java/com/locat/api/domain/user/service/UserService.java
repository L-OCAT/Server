package com.locat.api.domain.user.service;

import com.locat.api.domain.user.entity.User;
import java.util.Optional;

public interface UserService {

  User save(final User user);

  User findById(final Long id);

  Optional<User> findByOauthId(final String oauthId);
}
