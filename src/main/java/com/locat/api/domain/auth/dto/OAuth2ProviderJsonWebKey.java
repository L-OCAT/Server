package com.locat.api.domain.auth.dto;

/**
 * OAuth2 Provider JWT Web Key
 *
 * @param alg 토큰 암호화에 사용된 알고리즘
 * @param e RSA 공개 지수
 * @param kid 키 식별자(Auth Key ID)
 * @param kty 키 타입(RSA)
 * @param n RSA 모듈러스
 * @param use 키 사용 용도(Signature)
 */
public record OAuth2ProviderJsonWebKey(
    String alg, String e, String kid, String kty, String n, String use) {}
