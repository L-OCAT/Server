package com.locat.api.domain.admin.dto;

import jakarta.validation.constraints.NotEmpty;

public record AdminPasswordResetRequest(@NotEmpty String newPassword) {}
