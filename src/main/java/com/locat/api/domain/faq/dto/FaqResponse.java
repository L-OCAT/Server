package com.locat.api.domain.faq.dto;

import com.locat.api.domain.faq.entity.FAQ;
import com.locat.api.domain.faq.entity.FaqType;

/**
 * FAQ 정보 응답 DTO
 *
 * @param type FAQ 타입({@link FaqType})
 * @param title 제목
 * @param content 내용
 * @param createdAt 생성일
 * @param updatedAt 수정일
 */
public record FaqResponse(
    String type, String title, String content, String createdAt, String updatedAt) {

  public static FaqResponse fromEntity(FAQ faq) {
    return new FaqResponse(
        faq.getType().name(),
        faq.getTitle(),
        faq.getContent(),
        faq.getCreatedAt().toString(),
        faq.getUpdatedAt().toString());
  }
}
