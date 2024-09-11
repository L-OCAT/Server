package com.locat.api.global.security;

import com.locat.api.global.auth.LocatUserDetails;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** Auditing을 위한 현재 사용자 정보를 제공하는 클래스입니다.<br> */
@Component
public class LocatAuditorAware implements AuditorAware<Long> {

  public static final String PRINCIPAL_ANONYMOUS_USER = "anonymousUser";

  /**
   * Spring Security의 {@link SecurityContextHolder}를 사용하여 현재 사용자 정보를 가져옵니다. <br>
   * 인증 정보가 없거나, 인증되지 않은 경우 Optional.empty()를 반환합니다. <br>
   * 현재 컨텍스트에서 인증 정보를 가져오므로, 컨텍스트가 전파되지 않는 작업<i>(e.g. @Async)</i>에서는 사용이 불가능할 수 있습니다.
   *
   * @return 현재 사용자 정보
   */
  @Override
  public Optional<Long> getCurrentAuditor() {
    return Optional.of(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .filter(this::isNotAnonymous)
        .map(Authentication::getPrincipal)
        .map(LocatUserDetails.class::cast)
        .map(LocatUserDetails::getId);
  }

  private boolean isNotAnonymous(Authentication authentication) {
    return !PRINCIPAL_ANONYMOUS_USER.equalsIgnoreCase(authentication.getName());
  }
}
