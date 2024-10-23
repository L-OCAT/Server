package com.locat.api.global.advice;

import com.locat.api.global.annotation.AdminApi;
import com.locat.api.global.auth.LocatUserDetails;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminAuthorizationAdvice {

  @Before("@within(adminApi) || @annotation(adminApi)")
  public void enforceAdminAuthorization(JoinPoint joinPoint, AdminApi adminApi) {
    boolean onlySuperAdmin = this.getEffectiveOptions(joinPoint, adminApi);
    if (!this.hasRequiredAdminPrivilege(onlySuperAdmin)) {
      throw new AccessDeniedException("Admin(or SuperAdmin) previlege required");
    }
  }

  private boolean hasRequiredAdminPrivilege(boolean onlySuperAdmin) {
    return Optional.of(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .map(
            authentication -> {
              if (authentication.getPrincipal() instanceof LocatUserDetails locatUserDetails) {
                return onlySuperAdmin
                    ? locatUserDetails.isSuperAdmin()
                    : locatUserDetails.isAdmin();
              }
              return false;
            })
        .orElse(false);
  }

  /** 우선 순위(메서드 > 클래스)에 따라 선언된 {@code adminOnly} 값을 가져옵니다. */
  private boolean getEffectiveOptions(JoinPoint joinPoint, AdminApi adminApi) {
    return Optional.ofNullable(adminApi)
        .map(AdminApi::superAdminOnly)
        .orElseGet(
            () ->
                Optional.of(joinPoint)
                    .map(JoinPoint::getTarget)
                    .map(Object::getClass)
                    .map(clazz -> clazz.getAnnotation(AdminApi.class))
                    .map(AdminApi::superAdminOnly)
                    .orElse(false));
  }
}
