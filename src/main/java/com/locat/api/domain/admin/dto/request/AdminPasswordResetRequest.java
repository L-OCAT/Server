package com.locat.api.domain.admin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * 관리자 비밀번호 초기화 요청 DTO
 *
 * @param userId 관리자 이메일
 * @param newPassword 새 비밀번호
 */
public record AdminPasswordResetRequest(@Email String userId, @NotEmpty String newPassword) {}
