package com.locat.api.global.auth.jwt;

import com.locat.api.global.auth.LocatUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProviderImpl implements JwtProvider {

  public static final String BEARER_PREFIX = "Bearer ";
  public static final String AUTHORIZATION_KEY = "auth";

  public static final Duration ACCESS_TOKEN_EXPIRATION = Duration.ofHours(2);
  public static final Duration REFRESH_TOKEN_EXPIRATION = Duration.ofDays(14);

  private final LocatUserDetailsService userDetailsService;

  @Value("${auth.jwt.secret}")
  private String secretKey;

  private JwtParser parser;

  private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  /** secretKey를 Base64로 인코딩합니다. */
  @PostConstruct
  private void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    Key key = Keys.hmacShaKeyFor(bytes);
    this.parser = Jwts.parserBuilder().setSigningKey(key).build();
  }

  @Override
  public JwtDto create(String userEmail) {
    Authentication authentication = userDetailsService.createAuthentication(userEmail);
    String accessToken = this.createAccessTOken(authentication);
    String refreshToken = this.createRefreshToken();
    return JwtDto.jwtBuilder()
        .grantType(BEARER_PREFIX)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .accessTokenExpiresIn(ACCESS_TOKEN_EXPIRATION.toMillis())
        .create();
  }

  @Override
  public String resolve(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    }
    return null;
  }

  @Override
  public Claims parse(String token) {
    try {
      return parser.parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  @Override
  public void validate(String token) {
    try {
      parser.parseClaimsJws(token);
    } catch (MalformedJwtException
        | UnsupportedJwtException
        | ExpiredJwtException
        | IllegalArgumentException e) {
      log.warn("Invalid JWT token: {}", e.getMessage());
    }
  }

  private String createAccessTOken(Authentication authentication) {
    return Jwts.builder()
        .setSubject(authentication.getName())
        .claim(AUTHORIZATION_KEY, authentication.getAuthorities())
        .setExpiration(getExpirationDate(ACCESS_TOKEN_EXPIRATION))
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), signatureAlgorithm)
        .compact();
  }

  private String createRefreshToken() {
    return Jwts.builder()
        .setExpiration(getExpirationDate(REFRESH_TOKEN_EXPIRATION))
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), signatureAlgorithm)
        .compact();
  }

  private static Date getExpirationDate(Duration duration) {
    final long now = System.currentTimeMillis();
    return Date.from(Instant.ofEpochMilli(now + duration.toMillis()));
  }
}
