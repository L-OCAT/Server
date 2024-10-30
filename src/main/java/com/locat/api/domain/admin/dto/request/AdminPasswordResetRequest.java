package com.locat.api.domain.admin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AdminPasswordResetRequest(@Email String userId, @NotEmpty String newPassword) {}
