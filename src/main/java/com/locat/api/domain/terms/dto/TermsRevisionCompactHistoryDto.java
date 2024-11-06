package com.locat.api.domain.terms.dto;

import java.time.LocalDateTime;

public record TermsRevisionCompactHistoryDto(
    Double version, String revisionNote, LocalDateTime createdAt) {}
