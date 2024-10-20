package com.locat.api.global.auth.impl;

import com.locat.api.domain.user.entity.User;
import com.locat.api.global.auth.LocatUserDetails;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

public record LocatUserDetailsImpl(User user) implements LocatUserDetails, Serializable {

  @Serial private static final long serialVersionUID = 132581231927321L;

  public static LocatUserDetails from(User user) {
    return new LocatUserDetailsImpl(user);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(() -> this.user.getUserType().name());
  }

  @Override
  public User getUser() {
    return this.user;
  }

  @Override
  public Long getId() {
    return this.user.getId();
  }

  @Override
  public boolean isSuperAdmin() {
    return this.user.isSuperAdmin();
  }

  @Override
  public boolean isAdmin() {
    return this.user.isAdmin();
  }

  @Override
  public String getUsername() {
    return this.user.getEmail();
  }

  @Override
  public String getPassword() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isAccountNonExpired() {
    return LocatUserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return LocatUserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return LocatUserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return LocatUserDetails.super.isEnabled();
  }
}
