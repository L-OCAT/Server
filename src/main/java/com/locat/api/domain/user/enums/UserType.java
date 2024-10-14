package com.locat.api.domain.user.enums;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public enum UserType {
  USER,
  ADMIN,
  MANAGER;

  public static UserType fromValue(String value) {
    String rolePrefix = "ROLE_";
    if (value.startsWith(rolePrefix)) {
      value = value.substring(rolePrefix.length());
    }
    return UserType.valueOf(value.toUpperCase());
  }

  public static boolean isAdmin(Collection<? extends GrantedAuthority> authorities) {
    return authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .map(UserType::fromValue)
        .anyMatch(UserType::isAdmin);
  }

  public boolean isAdmin() {
    return this == ADMIN || this == MANAGER;
  }
}
