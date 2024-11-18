package com.locat.api.domain.user.service;

import com.locat.api.domain.user.dto.internal.EndpointRegisterDto;
import com.locat.api.domain.user.entity.association.UserEndpoint;
import java.util.List;

public interface UserEndpointService {

  /**
   * 사용자에게 디바이스 토큰과 플랫폼 정보가 일치하는 endpoint가 있는지 확인하고, 없을 시 생성하여 저장합니다.
   *
   * @param userId 등록하려는 사용자의 ID
   * @param registerDto 디바이스 토큰, 플랫폼 등 Endpoint 등록에 필요한 정보 DTO
   */
  void register(final Long userId, EndpointRegisterDto registerDto);

  /**
   * 사용자 ID로 사용자의 endpoint 목록을 조회합니다.
   *
   * @param userId 사용자 ID
   * @return 사용자의 endpoint 목록
   */
  List<UserEndpoint> findUserEndpointsByUserId(Long userId);
}
