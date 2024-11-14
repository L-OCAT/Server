package com.locat.api.domain.terms.dto.request;

import com.locat.api.domain.terms.entity.TermsType;
import jakarta.validation.constraints.NotBlank;

/**
 * 약관 등록 요청 DTO
 *
 * @param type 약관 타입({@link TermsType})
 * @param isRequired 필수 여부
 * @param title 약관 제목
 * @param content 약관 상세 내용
 * @param revisionNote 변경 사유
 */
public record TermsUpsertRequest(
    @NotBlank String type,
    boolean isRequired,
    @NotBlank String title,
    @NotBlank String content,
    @NotBlank String revisionNote) {}
