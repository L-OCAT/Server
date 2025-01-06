package com.locat.api.domain.user.service;

import com.locat.api.domain.user.dto.internal.UserRegisterDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.global.exception.custom.InternalProcessingException;

public interface UserTermsService {

  /**
   * 사용자 등록 정보를 기반으로, 약관 동의 여부를 확인 & 저장합니다.
   *
   * @param user 사용자 정보
   * @param registerDto 사용자 등록 정보
   * @throws InternalProcessingException 약관 동의 여부 저장 중 오류가 발생한 경우
   */
  void register(User user, UserRegisterDto registerDto);
}
