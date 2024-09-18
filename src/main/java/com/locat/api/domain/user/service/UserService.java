package com.locat.api.domain.user.service;

import com.locat.api.domain.user.dto.UserInfoUpdateDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.global.exception.NoSuchEntityException;
import java.util.Optional;

public interface UserService {

  User save(final User user);

  User update(final Long id, final UserInfoUpdateDto infoUpdateDto);

  /**
   * 사용자 ID로 사용자 조회
   *
   * @param id 사용자 ID
   * @return 사용자 정보
   * @throws NoSuchEntityException 해당 ID의 사용자가 없을 경우
   */
  User findById(final Long id);

  Optional<User> findByOAuthId(final String oAuthId);

  /**
   * 사용자 탈퇴
   *
   * @param id 사용자 ID
   * @param reason 탈퇴 사유
   */
  void delete(final Long id, final String reason);
}
