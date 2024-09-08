package com.locat.api.global.auth.impl;

import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.auth.LocatUserDetailsService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocatUserDetailsServiceImpl implements LocatUserDetailsService {

  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String userId) {
    return LocatUserDetailsImpl.from(this.userService.findById(Long.parseLong(userId)));
  }

  @Override
  public Authentication createAuthentication(String userId) {
    UserDetails userDetails = this.loadUserByUsername(userId);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  @Override
  public String extractAuthorities(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));
  }
}
