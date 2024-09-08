package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.user.entity.PlatformType;
import com.locat.api.domain.user.entity.User;
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

    @Override
    public void saveUserEndpoint(User user, String deviceToken, String platform,
                                 String endpointArn, String subscriptionArn) {
        PlatformType platformType = PlatformType.valueOf(platform);

        UserEndpoint userEndpoint = UserEndpoint.builder()
                .user(user)
                .deviceToken(deviceToken)
                .platformType(platformType)
                .endpointArn(endpointArn)
                .subscriptionArn(subscriptionArn)
                .build();

        this.userEndpointRepository.save(userEndpoint);
    }
}
