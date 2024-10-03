package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.user.dto.EndpointRegisterDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.UserEndpoint;
import com.locat.api.domain.user.service.PlatformEndpointService;
import com.locat.api.domain.user.service.UserEndpointService;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.infrastructure.repository.user.UserEndpointRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserEndpointServiceImpl implements UserEndpointService {

  private final UserEndpointRepository userEndpointRepository;
  private final PlatformEndpointService platformEndpointService;
  private final UserService userService;

  @Override
  public void register(Long userId, EndpointRegisterDto registerDto) {
    User user = this.userService.findById(userId);
    List<UserEndpoint> userEndpoints = this.findUserEndpointsByUserId(userId);

    if (this.isEndpointExists(registerDto, userEndpoints)) {
      return;
    }

    String endpointArn =
        this.platformEndpointService.create(registerDto.deviceToken(), registerDto.platformType());
    String subscriptionArn = this.platformEndpointService.subscribeToTopic(endpointArn);

    this.userEndpointRepository.save(
        UserEndpoint.of(user, endpointArn, subscriptionArn, registerDto));
  }

  @Override
  public List<UserEndpoint> findUserEndpointsByUserId(Long userId) {
    return this.userEndpointRepository.findByUserId(userId);
  }

  private boolean isEndpointExists(EndpointRegisterDto registerDto, List<UserEndpoint> endpoints) {
    return endpoints.stream()
        .anyMatch(
            endpoint -> endpoint.matches(registerDto.deviceToken(), registerDto.platformType()));
  }
}
