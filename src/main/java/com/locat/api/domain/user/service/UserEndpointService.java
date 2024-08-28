package com.locat.api.domain.user.service;

import java.util.List;

public interface UserEndpointService {
    List<String> findUserEndpointArnsByUserId(Long userId);
}
