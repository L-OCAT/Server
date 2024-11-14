package com.locat.api.global.security.jwt.impl;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.locat.api.domain.auth.entity.LocatRefreshToken;
import com.locat.api.domain.user.entity.User;
import com.locat.api.global.security.exception.TokenException;
import com.locat.api.global.security.jwt.JwtProvider;
import com.locat.api.global.security.jwt.dto.LocatTokenDto;
import com.locat.api.global.security.userdetails.LocatUserDetails;
import com.locat.api.global.security.userdetails.LocatUserDetailsService;
import com.locat.api.global.security.userdetails.impl.LocatUserDetailsImpl;
import com.locat.api.infra.redis.LocatRefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProviderImpl implements JwtProvider {

  public static final String AUTHORIZATION_KEY = "auth";
  public static final String USER_NAME_KEY = "name";
  public static final String BEARER_PREFIX = "Bearer ";
  public static final Duration ACCESS_TOKEN_EXPIRATION = Duration.ofHours(2);
  public static final Duration REFRESH_TOKEN_EXPIRATION = Duration.ofDays(14);

  private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

  @Value("${security.jwt.secret}")
  private String secretKey;

  @Value("${service.url.api}")
  private String serviceUrl;

  private Key key;

  private final Clock clock;
  private final LocatUserDetailsService userDetailsService;
  private final LocatRefreshTokenRepository refreshTokenRepository;

  @PostConstruct
  private void init() {
    byte[] bytes = Base64.getDecoder().decode(this.secretKey);
    this.key = Keys.hmacShaKeyFor(bytes);
  }

  @Override
  public LocatTokenDto create(final User user) {
    LocatUserDetails userDetails = LocatUserDetailsImpl.from(user);
    Authentication authentication = this.userDetailsService.createAuthentication(userDetails);

    String accessToken = this.createAccessToken(authentication);
    String refreshToken = this.createRefreshToken(authentication.getName());

    this.saveRefreshToken(userDetails, refreshToken);
    return LocatTokenDto.jwtBuilder()
        .grantType(BEARER_PREFIX)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .accessTokenExpiresIn(ACCESS_TOKEN_EXPIRATION.toSeconds())
        .refreshTokenExpiresIn(REFRESH_TOKEN_EXPIRATION.toSeconds())
        .create();
  }

  @Override
  public LocatTokenDto renew(String oldAccessToken, String refreshToken) {
    Claims claims = this.parse(oldAccessToken);

    this.validateRefreshToken(claims.getSubject(), refreshToken);
    Authentication authentication = this.userDetailsService.createAuthentication(claims);

    String newAccessToken = this.createAccessToken(authentication);
    return LocatTokenDto.jwtBuilder()
        .grantType(BEARER_PREFIX)
        .accessToken(newAccessToken)
        .accessTokenExpiresIn(ACCESS_TOKEN_EXPIRATION.toSeconds())
        .create();
  }

  private void validateRefreshToken(String userEmail, String refreshToken) {
    this.refreshTokenRepository
        .findByEmail(userEmail)
        .ifPresentOrElse(
            token -> {
              if (token.isNotMatched(refreshToken)) {
                throw new TokenException();
              }
            },
            () -> {
              throw new TokenException();
            });
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
      return Jwts.parserBuilder()
          .requireIssuer(this.serviceUrl)
          .setSigningKey(this.key)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException ex) {
      return ex.getClaims();
    } catch (JwtException ex) {
      log.debug("Could not parse JWT Cliams. / Reason: {}", ex.getMessage());
      throw new TokenException(ex);
    }
  }

  private String createAccessToken(Authentication authentication) {
    LocatUserDetails userDetails = (LocatUserDetails) authentication.getPrincipal();
    return Jwts.builder()
        .setSubject(authentication.getName())
        .addClaims(
            Map.of(
                AUTHORIZATION_KEY,
                this.userDetailsService.extractAuthority(authentication),
                USER_NAME_KEY,
                userDetails.getUser().getNickname()))
        .setIssuer(this.serviceUrl)
        .setIssuedAt(Date.from(Instant.now(this.clock)))
        .setExpiration(getExpirationDate(ACCESS_TOKEN_EXPIRATION))
        .signWith(this.key, signatureAlgorithm)
        .compact();
  }

  private String createRefreshToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuer(this.serviceUrl)
        .setIssuedAt(Date.from(Instant.now(this.clock)))
        .setExpiration(getExpirationDate(REFRESH_TOKEN_EXPIRATION))
        .signWith(Keys.hmacShaKeyFor(this.secretKey.getBytes()), signatureAlgorithm)
        .compact();
  }

  private void saveRefreshToken(LocatUserDetails userDetails, String refreshToken) {
    this.refreshTokenRepository.save(
        LocatRefreshToken.from(
            userDetails.getId(),
            userDetails.getUsername(),
            refreshToken,
            REFRESH_TOKEN_EXPIRATION));
  }

  private static Date getExpirationDate(Duration duration) {
    final long now = System.currentTimeMillis();
    return Date.from(Instant.ofEpochMilli(now + duration.toMillis()));
  }
}
