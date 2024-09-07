package com.locat.api.domain.user.service;

import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.UserEndpoint;

import java.util.List;

public interface UserEndpointService {
    List<UserEndpoint> findUserEndpointsByUserId(Long userId);

    void saveUserEndpoint(User user, String deviceToken, String platform, String EndpointArn);
}
