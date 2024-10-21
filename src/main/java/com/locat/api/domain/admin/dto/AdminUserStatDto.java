package com.locat.api.domain.admin.dto;

import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.domain.user.enums.StatusType;
import com.locat.api.domain.user.enums.UserType;
import java.time.LocalDateTime;
import java.util.List;

public record AdminUserStatDto(
    Long id,
    UserType type,
    OAuth2ProviderType oAuthType,
    String email,
    String nickname,
    StatusType statusType,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    List<AgreementDetail> agreementDetails,
    ActivityDetails activityDetails) {

  public record AgreementDetail(TermsType termsName, boolean isAgreed, LocalDateTime agreedAt) {}

  public record ActivityDetails(int totalRegisteredLostItems, int totalRegisteredFoundItems) {}
}
