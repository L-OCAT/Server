package com.locat.api.infrastructure.external;

import com.locat.api.domain.auth.template.OAuth2Properties;
import com.locat.api.domain.user.dto.KakaoUserInfoDto;
import com.locat.api.domain.user.dto.KakaoUserTermsAgreementDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoUserClient", url = "https://kapi.kakao.com")
public interface KakaoUserClient {

  @GetMapping("/v2/user/me")
  KakaoUserInfoDto getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);

  @GetMapping("/v2/user/me")
  KakaoUserInfoDto getUserInfoByAdmin(
      @RequestHeader(OAuth2Properties.KAKAO_ADMIN_KEY) String adminKey,
      @RequestParam("target_id_type") String targetIdType,
      @RequestParam("target_id") Long targetId);

  @GetMapping("/v2/user/service_terms")
  KakaoUserTermsAgreementDto fetchTermsAgreement(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);

  @GetMapping("/v2/user/service_terms")
  KakaoUserTermsAgreementDto fetchTermsAgreementByAdmin(
      @RequestHeader(OAuth2Properties.KAKAO_ADMIN_KEY) String adminKey);
}