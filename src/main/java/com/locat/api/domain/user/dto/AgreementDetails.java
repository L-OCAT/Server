package com.locat.api.domain.user.dto;

import com.locat.api.domain.terms.entity.TermsType;

public record AgreementDetails(
    TermsType termsType, Boolean required, Boolean agreed, Boolean revocable) {}
