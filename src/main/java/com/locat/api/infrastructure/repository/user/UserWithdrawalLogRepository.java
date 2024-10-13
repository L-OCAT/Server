package com.locat.api.infrastructure.repository.user;

import com.locat.api.domain.user.entity.association.UserWithdrawalLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWithdrawalLogRepository extends JpaRepository<UserWithdrawalLog, Long> {}
