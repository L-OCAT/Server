package com.locat.api.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AdminLoginRequest(
    @NotEmpty String deviceId, @Email String userId, @NotEmpty String password) {}
