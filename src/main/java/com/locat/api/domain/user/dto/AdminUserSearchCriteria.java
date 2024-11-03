package com.locat.api.domain.user.dto;

import java.time.LocalDate;

public record AdminUserSearchCriteria(
    String nickname, String email, LocalDate startDate, LocalDate endDate) {

  public static AdminUserSearchCriteria of(
      String nickname, String email, LocalDate startDate, LocalDate endDate) {
    return new AdminUserSearchCriteria(nickname, email, startDate, endDate);
  }
}
