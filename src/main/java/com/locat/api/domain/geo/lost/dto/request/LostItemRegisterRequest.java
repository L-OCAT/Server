package com.locat.api.domain.geo.lost.dto.request;

import com.locat.api.domain.geo.base.annotation.LostItemValidation;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * 분실물 등록 요청 DTO
 *
 * @param categoryId 카테고리 ID (직접 입력한 카테고리일 경우 null)
 * @param categoryName 카테고리 이름 (직접 입력한 카테고리일 경우 필수)
 * @param colorHexCode 색상 HEX 코드
 * @param itemName 분실물 이름
 * @param description 분실물 설명
 * @param isWillingToPayGratuity 보상금 지급 의사
 * @param gratuity 보상금(단위: %)
 * @param lat 분실 장소 위도
 * @param lng 분실 장소 경도
 */
@LostItemValidation
public record LostItemRegisterRequest(
    @Positive Long categoryId,
    String categoryName,
    @NotEmpty String colorHexCode,
    @NotEmpty String itemName,
    @NotEmpty String description,
    @NotNull Boolean isWillingToPayGratuity,
    Integer gratuity,
    Double lat,
    Double lng) {}
