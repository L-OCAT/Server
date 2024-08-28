package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.user.entity.UserEndpoint;
import com.locat.api.domain.user.service.UserEndpointService;
import com.locat.api.infrastructure.repository.user.UserEndpointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserEndpointServiceImpl implements UserEndpointService {

    private final UserEndpointRepository userEndpointRepository;
    @Override
    public List<String> findUserEndpointArnsByUserId(Long userId) {
        List<UserEndpoint> userEndpoints = this.userEndpointRepository.findByUserId(userId);

        if (userEndpoints.isEmpty()) {
            throw new RuntimeException("No user endpoint ARN found for userId=" + userId);
        }

        return userEndpoints.stream()
                .map(UserEndpoint::getEndpointArn)
                .toList();
    }
}
