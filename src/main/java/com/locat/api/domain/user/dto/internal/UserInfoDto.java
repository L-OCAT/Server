package com.locat.api.domain.user.dto.internal;

import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.domain.user.enums.StatusType;
import com.locat.api.domain.user.enums.UserType;
import java.time.LocalDateTime;

/**
 * 사용자 정보 DTO
 *
 * @param id 사용자 ID
 * @param type 사용자 타입
 * @param oAuthType OAuth2 제공자 타입
 * @param email 이메일
 * @param nickname 닉네임
 * @param statusType 사용자 상태
 * @param createdAt 생성일
 * @param updatedAt 수정일
 * @param deletedAt 삭제일
 */
public record UserInfoDto(
    Long id,
    UserType type,
    OAuth2ProviderType oAuthType,
    String email,
    String nickname,
    StatusType statusType,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt) {}
