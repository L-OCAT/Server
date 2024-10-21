package com.locat.api.domain.admin.service.impl;

import com.locat.api.domain.admin.dto.AdminUserStatDto;
import com.locat.api.domain.admin.service.AdminStatisticService;
import com.locat.api.domain.user.entity.EndUser;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
import com.locat.api.infrastructure.repository.user.EndUserRepository;
import com.locat.api.infrastructure.repository.user.UserQStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminStatisticServiceImpl implements AdminStatisticService {

  private final UserQStatisticRepository userQStatisticRepository;
  private final EndUserRepository endUserRepository;

  @Override
  public AdminUserStatDto getEndUserStat(Long id) {
    EndUser user =
        this.endUserRepository
            .findById(id)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
    return this.userQStatisticRepository.getUserStat(user);
  }
}
