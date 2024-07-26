package com.locat.api.global.auth;

import org.springframework.security.core.userdetails.UserDetails;

public interface LocatUserDetails extends UserDetails {

  Long getId();
}
