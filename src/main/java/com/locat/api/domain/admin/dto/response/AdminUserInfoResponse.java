package com.locat.api.domain.admin.dto.response;

import com.locat.api.domain.user.dto.internal.UserInfoDto;

/**
 * 관리자 사용자 조회 응답 DTO
 *
 * @param id 사용자 ID
 * @param type 구분 타입
 * @param oAuthType OAuth 가입 구분
 * @param email 이메일 주소
 * @param nickname 닉네임
 * @param statusType 사용자 상태
 * @param createdAt 가입일시
 * @param updatedAt 최근 수정일시
 * @param deletedAt 탈퇴일시
 */
public record AdminUserInfoResponse(
    Long id,
    String type,
    String oAuthType,
    String email,
    String nickname,
    String statusType,
    String createdAt,
    String updatedAt,
    String deletedAt) {

  public static AdminUserInfoResponse from(UserInfoDto dto) {
    return new AdminUserInfoResponse(
        dto.id(),
        dto.type().name(),
        dto.oAuthType().name(),
        dto.email(),
        dto.nickname(),
        dto.statusType().name(),
        dto.createdAt().toString(),
        dto.updatedAt().toString(),
        dto.deletedAt() != null ? dto.deletedAt().toString() : null);
  }
}
