package com.locat.api.global.security;

import static org.springframework.http.HttpMethod.*;

import com.locat.api.global.auth.LocatUserDetailsService;
import com.locat.api.global.auth.jwt.JwtProvider;
import com.locat.api.global.security.filter.ActiveUserFilter;
import com.locat.api.global.security.filter.JwtAuthenticationFilter;
import com.locat.api.global.security.filter.PublicApiKeyFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private static final List<String> DEFAULT_PERMIT_METHODS =
      List.of(
          GET.name(),
          HEAD.name(),
          POST.name(),
          PUT.name(),
          PATCH.name(),
          DELETE.name(),
          OPTIONS.name());

  private final SecurityProperties securityProperties;
  private final JwtProvider jwtProvider;
  private final LocatUserDetailsService userDetailsService;

  /** 상용(또는 개발) 환경에서 사용하는 SecurityFilterChain을 설정합니다. */
  @Bean
  @Profile("!local")
  protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
    return http.httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .requiresChannel(channel -> channel.anyRequest().requiresSecure())
        .cors(cors -> cors.configurationSource(this.corsConfigurationSource()))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(CorsUtils::isPreFlightRequest)
                    .permitAll()
                    .requestMatchers("/v*/**")
                    .permitAll()
                    .requestMatchers("/actuator/**")
                    .access(this.localHostOnly)
                    .anyRequest()
                    .denyAll())
        .addFilterBefore(
            new PublicApiKeyFilter(this.securityProperties.getApiKey()),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(
            new JwtAuthenticationFilter(this.jwtProvider, this.userDetailsService),
            PublicApiKeyFilter.class)
        .addFilterAfter(new ActiveUserFilter(), JwtAuthenticationFilter.class)
        .exceptionHandling(
            exception ->
                exception
                    .accessDeniedHandler(new LocatAccessDeniedHandler())
                    .authenticationEntryPoint(new LocatAuthEntryPoint()))
        .build();
  }

  /**
   * 개발 환경에서 사용하는 SecurityFilterChain을 설정합니다.
   *
   * <p>상용 SecurityFilterChain과 다른 점
   * <li>HTTPS를 요구하지 않음
   * <li>localhost의 모든 요청을 허용
   */
  @Bean
  @Profile("local")
  protected SecurityFilterChain configureLocal(HttpSecurity http) throws Exception {
    return http.httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(this.corsConfigurationSource()))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(CorsUtils::isPreFlightRequest)
                    .permitAll()
                    .anyRequest()
                    .access(this.localHostOnly))
        .addFilterBefore(
            new JwtAuthenticationFilter(this.jwtProvider, this.userDetailsService),
            UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  /** 비밀번호 등 해시화를 위한 PasswordEncoder */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /** CORS(Cross-Origin Resource Sharing) 설정 */
  @Bean
  protected CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.addAllowedOriginPattern("*");
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.setAllowedMethods(DEFAULT_PERMIT_METHODS);
    corsConfiguration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }

  /**
   * localhost의 요청만 허용하는 AuthorizationManager <br>
   * <li>Actuator, Prometheus 등 Monitoring을 위한 Endpoint에 대한 접근 제어 등에 사용
   */
  protected AuthorizationManager<RequestAuthorizationContext> localHostOnly =
      (auth, context) -> {
        String ip = context.getRequest().getRemoteAddr();
        boolean isLocalHost = ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1");
        return new AuthorizationDecision(isLocalHost);
      };
}
