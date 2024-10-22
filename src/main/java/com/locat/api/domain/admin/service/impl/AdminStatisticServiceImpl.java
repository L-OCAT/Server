package com.locat.api.domain.admin.service.impl;

import com.locat.api.domain.admin.dto.AdminUserFoundItemStatDto;
import com.locat.api.domain.admin.dto.AdminUserLostItemStatDto;
import com.locat.api.domain.admin.dto.AdminUserStatDto;
import com.locat.api.domain.admin.service.AdminStatisticService;
import com.locat.api.domain.geo.found.service.FoundItemService;
import com.locat.api.domain.geo.lost.service.LostItemService;
import com.locat.api.domain.user.entity.EndUser;
import com.locat.api.domain.user.service.EndUserService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
import com.locat.api.infrastructure.repository.user.UserQStatisticRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminStatisticServiceImpl implements AdminStatisticService {

  private final UserQStatisticRepository userQStatisticRepository;
  private final EndUserService endUserService;
  private final FoundItemService foundItemService;
  private final LostItemService lostItemService;

  @Override
  public AdminUserStatDto getEndUserStat(Long userId) {
    EndUser user = this.findUser(userId);
    return this.userQStatisticRepository.getUserStat(user);
  }

  @Override
  public List<AdminUserFoundItemStatDto> getUserFoundItemStat(Long userId) {
    EndUser user = this.findUser(userId);
    return this.foundItemService.findTop10ByEndUser(user).stream()
        .map(AdminUserFoundItemStatDto::fromEntity)
        .toList();
  }

  @Override
  public List<AdminUserLostItemStatDto> getUserLostItemStat(Long userId) {
    EndUser user = this.findUser(userId);
    return this.lostItemService.findTop10ByEndUser(user).stream()
        .map(AdminUserLostItemStatDto::fromEntity)
        .toList();
  }

  private EndUser findUser(Long userId) {
    return this.endUserService
        .findById(userId)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
  }
}
