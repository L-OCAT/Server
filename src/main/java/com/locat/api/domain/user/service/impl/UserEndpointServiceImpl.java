package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.user.dto.EndpointRegistrationRequest;
import com.locat.api.domain.user.entity.PlatformType;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.UserEndpoint;
import com.locat.api.domain.user.service.PlatformEndpointService;
import com.locat.api.domain.user.service.UserEndpointService;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.auth.LocatUserDetails;
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
  public void register(EndpointRegistrationRequest request, LocatUserDetails userDetails) {
    List<UserEndpoint> endpoints = this.findUserEndpointsByUserId(userDetails.getId());
    boolean endpointExists = this.isEndpointExists(request, endpoints);

    if (!endpointExists) {
      String endpointArn =
          this.platformEndpointService.create(request.deviceToken(), request.platform());
      String subscriptionArn = this.platformEndpointService.subscribeToTopic(endpointArn);

      User user = this.userService.findById(userDetails.getId());
      this.saveUserEndpoint(
          user, request.deviceToken(), request.platform(), endpointArn, subscriptionArn);
    }
  }

  @Override
  public List<UserEndpoint> findUserEndpointsByUserId(Long userId) {
    return this.userEndpointRepository.findByUserId(userId);
  }

  @Override
  public void saveUserEndpoint(
      User user, String deviceToken, String platform, String endPointArn, String subscriptionArn) {
    PlatformType platformType = PlatformType.valueOf(platform);

    UserEndpoint userEndpoint =
        UserEndpoint.builder()
            .user(user)
            .deviceToken(deviceToken)
            .platformType(platformType)
            .endpointArn(endPointArn)
            .subscriptionArn(subscriptionArn)
            .build();

    this.userEndpointRepository.save(userEndpoint);
  }

  private boolean isEndpointExists(
      EndpointRegistrationRequest request, List<UserEndpoint> endpoints) {
    return endpoints.stream()
        .anyMatch(
            e ->
                e.getDeviceToken().equals(request.deviceToken())
                    && e.getPlatformType().getValue().equals(request.platform()));
  }
}
