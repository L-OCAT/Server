package com.locat.api.global.security.userdetails.impl;

import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import com.locat.api.global.security.userdetails.LocatUserDetailsService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocatUserDetailsServiceImpl implements LocatUserDetailsService {

  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) {
    User user =
        this.userService
            .findByEmail(username)
            .filter(User::isActivated)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
    return LocatUserDetailsImpl.from(user);
  }

  @Override
  public Authentication createAuthentication(UserDetails userDetails) {
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  @Override
  public Authentication createAuthentication(Claims claims) {
    String userEmail = claims.getSubject();
    UserDetails userDetails = this.loadUserByUsername(userEmail);
    return this.createAuthentication(userDetails);
  }

  @Override
  public String extractAuthority(Authentication authentication) {
    return authentication.getAuthorities().iterator().next().getAuthority();
  }
}
