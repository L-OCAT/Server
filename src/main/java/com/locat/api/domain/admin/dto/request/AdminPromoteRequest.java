package com.locat.api.domain.admin.dto.request;

import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Range;

/**
 * 관리자 권한 부여 요청 DTO
 *
 * @param id 사용자 ID
 * @param level 부여할 권한 레벨
 */
public record AdminPromoteRequest(@Positive Long id, @Range(min = 0, max = 10) Integer level) {}
