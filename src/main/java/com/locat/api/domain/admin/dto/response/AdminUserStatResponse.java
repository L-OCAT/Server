package com.locat.api.domain.admin.dto.response;

import com.locat.api.domain.admin.dto.AdminUserStatDto;
import java.util.List;
import java.util.Objects;
import lombok.Builder;

/**
 * 관리자용 사용자 통계 정보 응답
 *
 * @param id 사용자 ID
 * @param type 사용자 유형
 * @param oAuthType OAuth2 제공자 유형
 * @param email 이메일
 * @param nickname 닉네임
 * @param statusType 상태 유형
 * @param createdAt 생성 일시
 * @param updatedAt 최종 수정 일시
 * @param deletedAt 삭제 일시
 * @param agreementDetails 약관 동의 정보
 * @param activityDetails 활동 정보
 */
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
        .deletedAt(Objects.toString(userStat.deletedAt(), "-"))
        .agreementDetails(userStat.agreementDetails().stream().map(AgreementDetail::from).toList())
        .activityDetails(ActivityDetails.from(userStat.activityDetails()))
        .build();
  }

  /**
   * 사용자의 약관 동의 정보
   *
   * @param termsName 약관 이름
   * @param isAgreed 동의 여부
   * @param agreedAt 동의 일시
   */
  @Builder
  record AgreementDetail(String termsName, boolean isAgreed, String agreedAt) {

    static AgreementDetail from(AdminUserStatDto.AgreementDetail agreementDetail) {
      return AgreementDetail.builder()
          .termsName(agreementDetail.termsName().name())
          .isAgreed(agreementDetail.isAgreed())
          .agreedAt(Objects.toString(agreementDetail.agreedAt(), "-"))
          .build();
    }
  }

  /**
   * 사용자의 활동 정보
   *
   * @param totalRegisteredLostItems 총 등록한 분실물 개수
   * @param totalRegisteredFoundItems 총 등록한 습득물 개수
   */
  record ActivityDetails(int totalRegisteredLostItems, int totalRegisteredFoundItems) {

    static ActivityDetails from(AdminUserStatDto.ActivityDetails activityDetails) {
      return new ActivityDetails(
          activityDetails.totalRegisteredLostItems(), activityDetails.totalRegisteredLostItems());
    }
  }
}
