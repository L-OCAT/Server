package com.locat.api.global.security.manager;

import com.locat.api.domain.user.enums.UserType;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@RequiredArgsConstructor
public class ActuatorAuthorizationManager
    implements AuthorizationManager<RequestAuthorizationContext> {

  private static final AuthorizationDecision PERMIT = new AuthorizationDecision(true);
  private static final AuthorizationDecision DENY = new AuthorizationDecision(false);

  private final String adminUrl;

  @Override
  public AuthorizationDecision check(
      Supplier<Authentication> authentication, RequestAuthorizationContext context) {
    final boolean isGranted =
        this.isAdministrator(authentication.get()) && this.isRequestFromAdmin(context);
    return isGranted ? PERMIT : DENY;
  }

  private boolean isAdministrator(Authentication authentication) {
    return UserType.isAdmin(authentication.getAuthorities()) && authentication.isAuthenticated();
  }

  private boolean isRequestFromAdmin(RequestAuthorizationContext context) {
    return this.adminUrl.equals(context.getRequest().getRemoteHost());
  }
}
