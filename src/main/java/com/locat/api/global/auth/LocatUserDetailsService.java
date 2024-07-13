package com.locat.api.global.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface LocatUserDetailsService extends UserDetailsService {

  Authentication createAuthentication(String username);

  String extractAuthorities(Authentication authentication);
}
