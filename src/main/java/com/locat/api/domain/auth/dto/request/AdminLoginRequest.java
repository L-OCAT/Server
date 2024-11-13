package com.locat.api.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * 관리자 로그인 요청
 *
 * @param deviceId 접속한 디바이스의 고유 ID
 * @param userId 사용자 ID(이메일)
 * @param password 사용자 비밀번호
 */
public record AdminLoginRequest(
    @NotEmpty String deviceId, @Email String userId, @NotEmpty String password) {}
