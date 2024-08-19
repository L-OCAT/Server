package com.locat.api.infrastructure.redis;

import com.locat.api.domain.auth.entity.VerificationCode;
import org.springframework.data.repository.CrudRepository;

public interface VerificationCodeRepository extends CrudRepository<VerificationCode, String> {}
