package com.locat.api.domain.user.enums;

public enum UserType {
  USER,
  ADMIN,
  MANAGER;

  public boolean isAdmin() {
    return this == ADMIN || this == MANAGER;
  }
}
