package com.locat.api.domain.user.dto.request;

import jakarta.validation.constraints.Email;

public record UserInfoUpdateRequest(@Email String email, String nickname) {}
