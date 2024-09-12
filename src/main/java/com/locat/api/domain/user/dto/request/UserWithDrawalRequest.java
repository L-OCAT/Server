package com.locat.api.domain.user.dto.request;

import jakarta.validation.constraints.Size;

/**
 * 사용자 탈퇴 요청 Request DTO
 *
 * @param reason 탈퇴 사유
 */
public record UserWithDrawalRequest(@Size(min = 1, max = 500) String reason) {}
