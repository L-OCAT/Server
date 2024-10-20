package com.locat.api.domain.user.dto.response;

import com.locat.api.domain.user.entity.User;

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
public record AdminUserResponse(
    Long id,
    String type,
    String oAuthType,
    String email,
    String nickname,
    String statusType,
    String createdAt,
    String updatedAt,
    String deletedAt) {

  public static AdminUserResponse fromEntity(User user) {
    return new AdminUserResponse(
        user.getId(),
        user.getUserType().getRoleName(),
        user.asEndUser().getOauthType().name(),
        user.getEmail(),
        user.getNickname(),
        user.getStatusType().name(),
        user.getCreatedAt().toString(),
        user.getUpdatedAt().toString(),
        user.getDeletedAt().toString());
  }
}
