package com.locat.api.domain.terms.dto;

import com.locat.api.domain.terms.entity.TermsType;
import jakarta.validation.constraints.NotBlank;

/**
 * 약관 등록 요청 DTO
 *
 * @param type 약관 타입({@link TermsType})
 * @param title 약관 제목
 * @param content 약관 상세 내용
 */
public record TermsRegisterRequest(
    @NotBlank String type, @NotBlank String title, @NotBlank String content) {}
