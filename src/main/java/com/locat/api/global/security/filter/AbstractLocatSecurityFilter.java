package com.locat.api.global.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.web.filter.OncePerRequestFilter;

public abstract class AbstractLocatSecurityFilter extends OncePerRequestFilter {

  /** Public API로 간주되는 요청 헤더 이름 */
  public static final String API_KEY_HEADER = "Locat-API-Key";

  /**
   * Public API로 간주되는 URI 목록 <br>
   * <li>{@code startWith} 메서드를 사용하여 URI를 비교합니다.
   */
  protected static final List<String> PUBLIC_API_PATHS = List.of("/v1/auth");

  /**
   * OAuth2 리다이렉트 URI <br>
   * Filter에서 제외되어야함
   */
  protected static final String OAUTH2_REDIRECT_PATH = "/v1/oauth2/redirect";

  protected boolean isPublicApi(HttpServletRequest request) {
    return PUBLIC_API_PATHS.stream().anyMatch(request.getRequestURI()::startsWith);
  }

  protected boolean isOAuthRedirect(HttpServletRequest request) {
    return request.getRequestURI().startsWith(OAUTH2_REDIRECT_PATH);
  }
}
