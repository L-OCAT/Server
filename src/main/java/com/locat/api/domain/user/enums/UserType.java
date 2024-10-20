package com.locat.api.domain.user.enums;

import java.util.Arrays;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum UserType {
  SUPER_ADMIN(0, "최고 관리자"),
  ADMIN(1, "관리자"),
  MANAGER(2, "매니저"),
  USER(10, "사용자");

  private final int level;
  private final String roleName;

  public static UserType fromValue(String value) {
    String rolePrefix = "ROLE_";
    if (value.startsWith(rolePrefix)) {
      value = value.substring(rolePrefix.length());
    }
    return UserType.valueOf(value.toUpperCase());
  }

  public static UserType fromLevel(final int level) {
    return Arrays.stream(UserType.values())
        .filter(userType -> userType.level == level)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid level"));
  }

  public static boolean isAdmin(Collection<? extends GrantedAuthority> authorities) {
    return authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .map(UserType::fromValue)
        .anyMatch(UserType::isAdmin);
  }

  public boolean isSuperAdmin() {
    return this == SUPER_ADMIN;
  }

  public boolean isAdmin() {
    return this.level < USER.level;
  }
}
