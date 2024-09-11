package com.locat.api.domain.user.service;

import com.locat.api.domain.user.dto.EndpointRegistrationRequest;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.UserEndpoint;
import com.locat.api.global.auth.LocatUserDetails;
import java.util.List;

public interface UserEndpointService {

  /**
   * 사용자에게 디바이스 토큰과 플랫폼 정보가 일치하는 endpoint가 있는지 확인하고, 없을 시 생성하여 저장합니다.
   *
   * @param request 디바이스 토큰, 플랫폼 정보
   * @param userDetails 유저 정보
   */
  void register(EndpointRegistrationRequest request, LocatUserDetails userDetails);

  List<UserEndpoint> findUserEndpointsByUserId(Long userId);

  void saveUserEndpoint(
      User user, String deviceToken, String platform, String endPointArn, String subscriptionArn);
}
