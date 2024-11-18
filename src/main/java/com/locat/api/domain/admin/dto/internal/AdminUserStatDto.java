package com.locat.api.domain.admin.dto.internal;

import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.domain.user.enums.StatusType;
import com.locat.api.domain.user.enums.UserType;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 관리자용 사용자 통계 정보 응답 DTO
 *
 * @param id 사용자 ID
 * @param type 사용자 타입
 * @param oAuthType OAuth2 제공자 타입
 * @param email 이메일
 * @param nickname 닉네임
 * @param statusType 사용자 상태
 * @param createdAt 사용자 등록일시
 * @param updatedAt 사용자 정보 수정일시
 * @param deletedAt 사용자 삭제일시
 * @param agreementDetails 사용자가 동의한 약관 정보
 * @param activityDetails 사용자 활동 정보
 */
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

  /**
   * 사용자가 동의한 약관 정보
   *
   * @param termsName 약관명
   * @param isAgreed 동의 여부
   * @param agreedAt 동의 일시
   */
  public record AgreementDetail(TermsType termsName, boolean isAgreed, LocalDateTime agreedAt) {}

  /**
   * 사용자 활동 정보
   *
   * @param totalRegisteredLostItems 총 등록한 분실물 수
   * @param totalRegisteredFoundItems 총 등록한 습득물 수
   */
  public record ActivityDetails(int totalRegisteredLostItems, int totalRegisteredFoundItems) {}
}
