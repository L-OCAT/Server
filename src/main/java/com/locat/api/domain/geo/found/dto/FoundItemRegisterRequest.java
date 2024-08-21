package com.locat.api.domain.geo.found.dto;

/**
 * 습득물 등록 요청 DTO
 *
 * @param isCustomCategory 사용자가 직접 입력한 카테고리인지 여부
 * @param categoryId 카테고리 ID (isCustomCategory가 false일 경우 필수)
 * @param categoryName 카테고리 이름 (isCustomCategory가 true일 경우 필수)
 * @param isCustomColor 사용자가 직접 입력한 습득물 색상인지 여부
 * @param colorId 색상 ID (isCustomColor가 false일 경우 필수)
 * @param colorName 색상 이름 (isCustomColor가 true일 경우 필수)
 * @param itemName 습득물 이름
 * @param description 습득물 설명
 * @param custodyLocation 보관 장소
 * @param latitude 습득 장소 위도
 * @param longitude 습득 장소 경도
 */
public record FoundItemRegisterRequest(
    Boolean isCustomCategory,
    Long categoryId,
    String categoryName,
    Boolean isCustomColor,
    Long colorId,
    String colorName,
    String itemName,
    String description,
    String custodyLocation,
    Double latitude,
    Double longitude) {}
