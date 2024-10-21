package com.locat.api.domain.admin.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record AdminPasswordResetRequest(@NotEmpty String newPassword) {}
