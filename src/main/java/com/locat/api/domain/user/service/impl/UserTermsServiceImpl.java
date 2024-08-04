package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.user.dto.AgreementDetails;
import com.locat.api.domain.user.dto.KakaoUserTermsAgreementDto;
import com.locat.api.domain.user.dto.OAuth2ProviderTermsAgreementDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.UserTermsAgreement;
import com.locat.api.domain.user.service.UserTermsService;
import com.locat.api.global.constant.AuthConstant;
import com.locat.api.infrastructure.external.KakaoUserClient;
import com.locat.api.infrastructure.repository.terms.TermsRepository;
import com.locat.api.infrastructure.repository.user.UserTermsAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserTermsServiceImpl implements UserTermsService {

  private final KakaoUserClient kakaoUserClient;
  private final TermsRepository termsRepository;
  private final UserTermsAgreementRepository userTermsAgreementRepository;

  @Override
  public void registerByOAuth(User user, OAuth2ProviderToken token) {
    List<UserTermsAgreement> userTermsAgreements =
        this.getAgreementByOAuthType(token).getAgreementDetails().stream()
            .map(AgreementDetails::termsType)
            .map(termsRepository::findByType)
            .filter(Optional::isPresent)
            .map(agreement -> UserTermsAgreement.of(user, agreement.get()))
            .toList();
    this.userTermsAgreementRepository.saveAll(userTermsAgreements);
  }

  private OAuth2ProviderTermsAgreementDto getAgreementByOAuthType(OAuth2ProviderToken token) {
    return switch (token.getProviderType()) {
      case KAKAO -> this.getKakaoAgreementInfo(token);
      case APPLE -> this.getAppleAgreementInfo(token);
    };
  }

  private KakaoUserTermsAgreementDto getKakaoAgreementInfo(OAuth2ProviderToken token) {
    return this.kakaoUserClient.fetchTermsAgreement(
        AuthConstant.BEARER_PREFIX + token.getAccessToken());
  }

  private OAuth2ProviderTermsAgreementDto getAppleAgreementInfo(OAuth2ProviderToken ignored) {
    throw new UnsupportedOperationException("Apple OAuth2ProviderType is not supported yet");
  }
}
