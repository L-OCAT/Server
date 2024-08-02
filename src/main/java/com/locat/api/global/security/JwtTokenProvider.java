package com.locat.api.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${auth.jwt.secret}")
    private String jwtSecret;
    private SecretKey secretKey;
    private final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;
    private final long REFRESH_TOKEN_EXPIRE_TIME =  7 * 24 * 60 * 60 * 1000L;

    /**
     * secret key byte 배열로 변환 후,
     * hmac 알고리즘에서 사용하기 적합한 SecretKey 객체 생성
     */
    @PostConstruct
    private void setSecretKey() {
        this.secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes());
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
    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
    }

    /**
     * Token 생성 공통 메소드
     * @param authentication: 사용자 정보
     * @param expireTime: 토큰별 만료 기간
     * @return 생성된 JWT
     */
    private String generateToken(Authentication authentication, long expireTime) {
        // 사용자 정보 가져오기
        UserDetails principal = (UserDetails) authentication.getPrincipal();    // 소셜로그인에 맞는 userdetails 구현 필요
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
}
