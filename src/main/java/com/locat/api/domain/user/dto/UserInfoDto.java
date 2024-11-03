package com.locat.api.domain.user.dto;

import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.domain.user.enums.StatusType;
import com.locat.api.domain.user.enums.UserType;
import java.time.LocalDateTime;

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
