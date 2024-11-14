package com.locat.api.infra.persistence.user;

import com.locat.api.domain.user.entity.association.UserTermsAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTermsAgreementRepository extends JpaRepository<UserTermsAgreement, Long> {}
