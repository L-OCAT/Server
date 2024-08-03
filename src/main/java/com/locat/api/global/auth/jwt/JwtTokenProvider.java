package com.locat.api.global.auth.jwt;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.locat.api.global.constant.AuthConstant.BEARER_PREFIX;
import static com.locat.api.global.exception.ApiExceptionType.INVALID_TOKEN;
import static com.locat.api.global.exception.ApiExceptionType.JWT_TOKEN_INVALID;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${auth.jwt.secret}")
    private String jwtSecret;
    private SecretKey secretKey;
    private final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;
    private final long REFRESH_TOKEN_EXPIRE_TIME =  7 * 24 * 60 * 60 * 1000L;
    private JwtParser parser;

    /**
     * secret key 디코딩 후 byte 배열로 변환.
     * hmac 알고리즘에서 사용하기 적합한 SecretKey 객체 생성
     * JwtParser 생성 - 서명에 사용할 key 설정
     */
    @PostConstruct
    private void init() {
        byte[] bytes = Base64.getDecoder().decode(this.jwtSecret);
        this.secretKey = Keys.hmacShaKeyFor(bytes);
        this.parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    /**
     * @param authentication: 사용자 정보
     * @return 생성된 access token
     */
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
    }

    /**
     * @param authentication: 사용자 정보
     * @return 생성된 refresh token
     */
    public String generateRefreshToken(Authentication authentication, String accessToken) {
        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
        // (accessToken, refreshToken) 저장 - redis?
        return refreshToken;
    }

    /**
     * Token 생성 공통 메소드
     * @param authentication: 사용자 정보
     * @param expireTime: 토큰별 만료 기간
     * @return 생성된 JWT
     */
    private String generateToken(Authentication authentication, long expireTime) {
        // 사용자 정보 가져오기
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        String username = principal.getUsername();      // email을 식별자로 사용. (or oauthId)
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)        // user type -> grantedauthority로
                .toList();

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(this.secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * HTTP 요청에서 JWT token 추출
     * @param request: HTTP 요청
     * @return 요청에 포함된 token
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);  // AuthConstant에는 값이 "auth"로 되어 있음.
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    /**
     * @param token: Jwt Token
     * @return token의 만료 여부
     */
    public boolean validateToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().after(new Date());
    }

    /**
     * token이 유효한 형태인지 확인하고 exception 처리
     * @param token: Jwt Token
     * @return token에 포함된 Claims 정보
     */
    private Claims parseToken(String token) {
        try {
            return this.parser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) { // 만료된 토큰이면 재발급을 위해 claims 정보 반환
            return e.getClaims();
        } catch (MalformedJwtException | UnsupportedJwtException | SecurityException e) {
            throw new TokenException(INVALID_TOKEN);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseToken(token);
        List<SimpleGrantedAuthority> roles = getAuthorities(claims);

        User principal = new User(claims.getSubject(), null, roles);
        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(
                claims.get("roles").toString()));
    }

    public String reissueAccessToken(String refreshToken) {
        // Refresh token이 유효한지 확인
        if (validateToken(refreshToken)) {
            Claims claims = parseToken(refreshToken);
            List<SimpleGrantedAuthority> roles = getAuthorities(claims);

            User principal = new User(claims.getSubject(), null, roles);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principal, refreshToken, principal.getAuthorities());

            return generateAccessToken(authentication);
        }
        return null; // 유효하지 않은 경우 null 또는 예외 발생
    }
}
