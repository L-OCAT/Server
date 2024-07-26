package com.locat.api.domain.user.service;

import com.locat.api.domain.user.entity.User;

import java.util.Optional;

public interface UserService {

  Optional<User> findByEmail(String email);
}
