package com.locat.api.global.security;

import static org.springframework.http.HttpMethod.*;

import com.locat.api.global.security.filter.SecurityFilterFactory;
import com.locat.api.global.security.filter.impl.JwtAuthenticationFilter;
import com.locat.api.global.security.filter.impl.PublicApiAccessControlFilter;
import com.locat.api.global.security.handler.LocatAccessDeniedHandler;
import com.locat.api.global.security.handler.LocatAuthEntryPoint;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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

  private final SecurityFilterFactory filterFactory;

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
                    .hasAuthority("SUPER_ADMIN")
                    .anyRequest()
                    .denyAll())
        .addFilterBefore(
            this.filterFactory.publicAccess(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(this.filterFactory.jwtAuth(), PublicApiAccessControlFilter.class)
        .addFilterAfter(this.filterFactory.adminAuth(), JwtAuthenticationFilter.class)
        .exceptionHandling(
            exception ->
                exception
                    .accessDeniedHandler(new LocatAccessDeniedHandler())
                    .authenticationEntryPoint(new LocatAuthEntryPoint()))
        .build();
  }

  /**
   * 로컬 환경에서 사용하는 SecurityFilterChain을 설정합니다.
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
                    .requestMatchers("/actuator/**")
                    .hasAuthority("SUPER_ADMIN")
                    .anyRequest()
                    .permitAll())
        .addFilterBefore(
            this.filterFactory.publicAccess(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(this.filterFactory.jwtAuth(), PublicApiAccessControlFilter.class)
        .addFilterAfter(this.filterFactory.adminAuth(), JwtAuthenticationFilter.class)
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
}
