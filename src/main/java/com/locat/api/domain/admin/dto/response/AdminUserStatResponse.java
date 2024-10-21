package com.locat.api.domain.admin.dto.response;

import com.locat.api.domain.admin.dto.AdminUserStatDto;
import java.util.List;
import lombok.Builder;

@Builder
public record AdminUserStatResponse(
    Long id,
    String type,
    String oAuthType,
    String email,
    String nickname,
    String statusType,
    String createdAt,
    String updatedAt,
    String deletedAt,
    List<AgreementDetail> agreementDetails,
    ActivityDetails activityDetails) {

  public static AdminUserStatResponse from(AdminUserStatDto userStat) {
    return AdminUserStatResponse.builder()
        .id(userStat.id())
        .type(userStat.type().name())
        .oAuthType(userStat.oAuthType().name())
        .email(userStat.email())
        .nickname(userStat.nickname())
        .statusType(userStat.statusType().name())
        .createdAt(userStat.createdAt().toString())
        .updatedAt(userStat.updatedAt().toString())
        .deletedAt(userStat.deletedAt() != null ? userStat.deletedAt().toString() : null)
        .agreementDetails(userStat.agreementDetails().stream().map(AgreementDetail::from).toList())
        .activityDetails(ActivityDetails.from(userStat.activityDetails()))
        .build();
  }

  @Builder
  record AgreementDetail(String termsName, boolean isAgreed, String agreedAt) {

    static AgreementDetail from(AdminUserStatDto.AgreementDetail agreementDetail) {
      return AgreementDetail.builder()
          .termsName(agreementDetail.termsName().name())
          .isAgreed(agreementDetail.isAgreed())
          .agreedAt(agreementDetail.agreedAt().toString())
          .build();
    }
  }

  record ActivityDetails(int totalRegisteredLostItems, int totalRegisteredFoundItems) {

    static ActivityDetails from(AdminUserStatDto.ActivityDetails activityDetails) {
      return new ActivityDetails(
          activityDetails.totalRegisteredLostItems(), activityDetails.totalRegisteredLostItems());
    }
  }
}
