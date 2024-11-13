package com.locat.api.domain.user.dto.request;

/**
 * 사용자 정보 수정 요청 DTO
 *
 * @param email 이메일
 * @param nickname 닉네임
 */
public record UserInfoUpdateRequest(String email, String nickname) {}
