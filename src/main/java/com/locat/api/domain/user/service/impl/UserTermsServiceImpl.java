package com.locat.api.domain.user.service.impl;

import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.domain.user.dto.UserRegisterDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.association.UserTermsAgreement;
import com.locat.api.domain.user.service.UserTermsService;
import com.locat.api.global.exception.InternalProcessingException;
import com.locat.api.infrastructure.repository.terms.TermsQRepository;
import com.locat.api.infrastructure.repository.user.UserTermsAgreementRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserTermsServiceImpl implements UserTermsService {

  private final TermsQRepository termsQRepository;
  private final UserTermsAgreementRepository userTermsAgreementRepository;

  @Override
  public void register(User user, UserRegisterDto registerDto) {
    List<UserTermsAgreement> userTermsAgreements = this.createUserTermsAgreement(user, registerDto);
    this.userTermsAgreementRepository.saveAll(userTermsAgreements);
  }

  private List<UserTermsAgreement> createUserTermsAgreement(
      User user, UserRegisterDto registerDto) {
    List<UserTermsAgreement> userTermsAgreements = new ArrayList<>();
    List<Terms> latestTermsList = this.termsQRepository.findAllLatest();
    if (Boolean.TRUE.equals(registerDto.isTermsOfServiceAgreed())) {
      Terms terms = this.getTermsByType(TermsType.TERMS_OF_SERVICE, latestTermsList);
      userTermsAgreements.add(UserTermsAgreement.of(user, terms));
    }
    if (Boolean.TRUE.equals(registerDto.isPrivacyPolicyAgreed())) {
      Terms terms = this.getTermsByType(TermsType.PRIVACY_POLICY, latestTermsList);
      userTermsAgreements.add(UserTermsAgreement.of(user, terms));
    }
    if (Boolean.TRUE.equals(registerDto.isLocationPolicyAgreed())) {
      Terms terms = this.getTermsByType(TermsType.LOCATION_POLICY, latestTermsList);
      userTermsAgreements.add(UserTermsAgreement.of(user, terms));
    }
    if (Boolean.TRUE.equals(registerDto.isMarketingPolicyAgreed())) {
      Terms terms = this.getTermsByType(TermsType.MARKETING_POLICY, latestTermsList);
      userTermsAgreements.add(UserTermsAgreement.of(user, terms));
    }
    return userTermsAgreements;
  }

  private Terms getTermsByType(TermsType termsType, List<Terms> termsList) {
    return termsList.stream()
        .filter(terms -> terms.getType().equals(termsType))
        .findFirst()
        .orElseThrow(
            () ->
                new InternalProcessingException(
                    String.format("Terms[%s] not registered!", termsType.name())));
  }
}
