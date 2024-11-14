package com.locat.api.global.security.userdetails;

import com.locat.api.domain.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface LocatUserDetails extends UserDetails {

  User getUser();

  Long getId();

  boolean isSuperAdmin();

  boolean isAdmin();
}
