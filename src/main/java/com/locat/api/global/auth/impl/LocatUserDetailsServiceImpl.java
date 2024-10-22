package com.locat.api.global.auth.impl;

import static com.locat.api.global.auth.jwt.JwtProviderImpl.AUTHORIZATION_KEY;

import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.enums.UserType;
import com.locat.api.domain.user.service.AdminUserService;
import com.locat.api.domain.user.service.EndUserService;
import com.locat.api.global.auth.LocatUserDetailsService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
import io.jsonwebtoken.Claims;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocatUserDetailsServiceImpl implements LocatUserDetailsService {

  private final EndUserService endUserService;
  private final AdminUserService adminUserService;

  @Override
  public UserDetails loadUserByUsername(String username) {
    User user =
        this.endUserService
            .findByEmail(username)
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
    UserType userType = this.parseUserType(claims);
    return userType.isAdmin()
        ? this.adminUserService
            .findByEmail(userEmail)
            .map(LocatUserDetailsImpl::from)
            .map(this::createAuthentication)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER))
        : this.endUserService
            .findByEmail(userEmail)
            .map(LocatUserDetailsImpl::from)
            .map(this::createAuthentication)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
  }

  private UserType parseUserType(Claims claims) {
    return Optional.of(claims)
        .map(c -> c.get(AUTHORIZATION_KEY, String.class))
        .map(UserType::fromValue)
        .orElse(UserType.USER);
  }

  @Override
  public String extractAuthority(Authentication authentication) {
    return authentication.getAuthorities().iterator().next().getAuthority();
  }
}
