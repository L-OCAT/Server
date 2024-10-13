package com.locat.api.domain.user.dto.response;

import com.locat.api.domain.user.entity.EndUser;

/**
 * 사용자 정보 조회 응답
 *
 * @param id 사용자 ID
 * @param email 이메일
 * @param nickname 닉네임
 * @param profileImageUrl 프로필 이미지 URL
 * @param createdAt 가입일시
 * @param updatedAt 최근 수정일시
 */
public record UserInfoResponse(
    Long id,
    String email,
    String nickname,
    String profileImageUrl,
    String createdAt,
    String updatedAt) {
  public static UserInfoResponse fromEntity(EndUser user) {
    return new UserInfoResponse(
        user.getId(),
        user.getEmail(),
        user.getNickname(),
        user.getProfileImage(),
        user.getCreatedAt().toString(),
        user.getUpdatedAt().toString());
  }
}
