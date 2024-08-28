package com.locat.api.domain.user.service;

import com.locat.api.domain.user.entity.UserEndpoint;

import java.util.List;

public interface UserEndpointService {
    List<UserEndpoint> findUserEndpointsByUserId(Long userId);
}
