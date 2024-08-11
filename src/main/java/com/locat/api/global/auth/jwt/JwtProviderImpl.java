package com.locat.api.global.auth.jwt;

import com.locat.api.domain.auth.entity.LocatRefreshToken;
import com.locat.api.global.auth.LocatUserDetails;
import com.locat.api.global.auth.LocatUserDetailsService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.infrastructure.redis.LocatRefreshTokenRepository;
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
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProviderImpl implements JwtProvider {

  public static final String AUTHORIZATION_KEY = "auth";
  public static final String BEARER_PREFIX = "Bearer ";
  public static final Duration ACCESS_TOKEN_EXPIRATION = Duration.ofHours(2);
  public static final Duration REFRESH_TOKEN_EXPIRATION = Duration.ofDays(14);

  private final Clock clock;
  private final LocatUserDetailsService userDetailsService;
  private final LocatRefreshTokenRepository refreshTokenRepository;

  @Value("${security.jwt.secret}")
  private String secretKey;

  private JwtParser parser;

  private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

  /** secretKey를 Base64로 인코딩합니다. */
  @PostConstruct
  private void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    Key key = Keys.hmacShaKeyFor(bytes);
    this.parser = Jwts.parserBuilder().setSigningKey(key).build();
  }

  @Override
  public LocatTokenDto create(String userEmail) {
    LocatUserDetails userDetails =
        (LocatUserDetails) userDetailsService.loadUserByUsername(userEmail);
    Authentication authentication = userDetailsService.createAuthentication(userEmail);

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
  public LocatTokenDto renew(String accessToken, String refreshToken) {
    Claims claims = this.parse(accessToken);
    String username = claims.getSubject();

    this.validateRefreshToken(username, refreshToken);
    Authentication authentication = userDetailsService.createAuthentication(username);

    String newAccessToken = this.createAccessToken(authentication);
    return LocatTokenDto.jwtBuilder()
        .grantType(BEARER_PREFIX)
        .accessToken(newAccessToken)
        .accessTokenExpiresIn(ACCESS_TOKEN_EXPIRATION.toSeconds())
        .create();
  }

  private void validateRefreshToken(String username, String refreshToken) {
    this.refreshTokenRepository
        .findByEmail(username)
        .ifPresentOrElse(
            t -> {
              if (t.isNotMatched(refreshToken)) {
                throw new TokenException(ApiExceptionType.INVALID_REFRESH_TOKEN);
              }
            },
            () -> {
              throw new TokenException(ApiExceptionType.INVALID_REFRESH_TOKEN);
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
      return parser.parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException ex) {
      return ex.getClaims();
    } catch (JwtException ex) {
      throw new TokenException();
    }
  }

  private String createAccessToken(Authentication authentication) {
    return Jwts.builder()
        .setSubject(authentication.getName())
        .claim(AUTHORIZATION_KEY, authentication.getAuthorities())
        .setIssuedAt(Date.from(Instant.now(clock)))
        .setExpiration(getExpirationDate(ACCESS_TOKEN_EXPIRATION))
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), signatureAlgorithm)
        .compact();
  }

  private String createRefreshToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(Date.from(Instant.now(clock)))
        .setExpiration(getExpirationDate(REFRESH_TOKEN_EXPIRATION))
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), signatureAlgorithm)
        .compact();
  }

  private void saveRefreshToken(LocatUserDetails userDetails, String refreshToken) {
    final long id = userDetails.getId();
    final String email = userDetails.getUsername();
    this.refreshTokenRepository.save(LocatRefreshToken.from(id, email, refreshToken));
  }

  private static Date getExpirationDate(Duration duration) {
    final long now = System.currentTimeMillis();
    return Date.from(Instant.ofEpochMilli(now + duration.toMillis()));
  }
}
