package com.locat.api.domain.admin.dto.request;

import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Range;

public record AdminPromoteRequest(@Positive Long id, @Range(min = 0, max = 10) Integer level) {}
