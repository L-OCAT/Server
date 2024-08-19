package com.locat.api.domain.user.service;

import com.locat.api.domain.user.dto.request.UserInfoUpdateRequest;
import jakarta.validation.constraints.Email;

public record UserInfoUpdateDto(@Email String email, String nickname) {

  public static UserInfoUpdateDto from(UserInfoUpdateRequest request) {
    return new UserInfoUpdateDto(request.email(), request.nickname());
  }
}
