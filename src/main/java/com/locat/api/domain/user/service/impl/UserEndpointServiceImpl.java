package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.user.entity.UserEndpoint;
import com.locat.api.domain.user.service.UserEndpointService;
import com.locat.api.infrastructure.repository.user.UserEndpointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserEndpointServiceImpl implements UserEndpointService {

    private final UserEndpointRepository userEndpointRepository;

    @Override
    public List<UserEndpoint> findUserEndpointsByUserId(Long userId) {

        return this.userEndpointRepository.findByUserId(userId);
    }
}
