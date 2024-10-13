package com.locat.api.domain.user.dto;

import com.locat.api.domain.user.enums.OAuth2ProviderType;
import java.util.List;

public interface OAuth2ProviderTermsAgreementDto {

  String getId();

  OAuth2ProviderType getProviderType();

  List<AgreementDetails> getAgreementDetails();
}
