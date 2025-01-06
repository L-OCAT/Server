package com.locat.api.domain.user.dto.criteria;

import java.time.LocalDate;

/**
 * 관리자용 사용자 검색 조건 DTO
 *
 * @param nickname 검색할 닉네임(Like 검색)
 * @param email 검색할 이메일(Eq 검색)
 * @param startDate 검색할 가입 시작일
 * @param endDate 검색할 가입 종료일
 */
public record AdminUserSearchCriteria(
    String nickname, String email, LocalDate startDate, LocalDate endDate) {

  public static AdminUserSearchCriteria of(
      String nickname, String email, LocalDate startDate, LocalDate endDate) {
    return new AdminUserSearchCriteria(nickname, email, startDate, endDate);
  }
}
