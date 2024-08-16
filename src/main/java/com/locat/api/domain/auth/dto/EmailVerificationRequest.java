package com.locat.api.domain.auth.dto;

import jakarta.validation.constraints.Email;

/**
 * 이메일 인증 요청 관련 DTO
 *
 * @param email 인증 메일을 발송하거나, 인증을 수행할 이메일
 * @param code 인증 코드
 */
public record EmailVerificationRequest(@Email String email, String code) {}
