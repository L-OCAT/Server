package com.locat.api.domain.user.service;

import com.locat.api.domain.user.dto.UserRegisterDto;
import com.locat.api.domain.user.entity.EndUser;

public interface UserTermsService {

  void register(EndUser user, UserRegisterDto registerDto);
}
