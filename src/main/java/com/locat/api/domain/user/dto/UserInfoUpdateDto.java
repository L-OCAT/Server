package com.locat.api.domain.user.dto;

import com.locat.api.domain.user.dto.request.UserInfoUpdateRequest;

/**
 * 사용자 정보 수정 DTO
 *
 * @param email 이메일
 * @param nickname 닉네임
 */
public record UserInfoUpdateDto(String email, String nickname) {

  public static UserInfoUpdateDto from(UserInfoUpdateRequest request) {
    return new UserInfoUpdateDto(request.email(), request.nickname());
  }
}
