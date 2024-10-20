package com.locat.api.domain.user.service;

import com.locat.api.domain.user.dto.UserInfoUpdateDto;
import com.locat.api.domain.user.entity.EndUser;
import com.locat.api.domain.user.entity.User;
import com.locat.api.global.exception.NoSuchEntityException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  Page<EndUser> findAll(Pageable pageable);

  /**
   * 사용자 ID로 사용자 조회
   *
   * @param id 사용자 ID
   * @return 사용자 정보
   * @throws NoSuchEntityException 해당 ID의 사용자가 없을 경우
   */
  Optional<User> findById(final Long id);

  /**
   * 사용자 이메일 해시로 사용자 조회
   *
   * @param email 사용자 이메일(Not Hashed)
   * @return 사용자 정보
   */
  Optional<User> findByEmail(final String email);

  EndUser save(final EndUser user);

  EndUser update(final Long id, final UserInfoUpdateDto infoUpdateDto);

  Optional<EndUser> findEndUserByOAuthId(final String oAuthId);

  /**
   * 사용자 탈퇴
   *
   * @param id 사용자 ID
   * @param reason 탈퇴 사유
   */
  void delete(final Long id, final String reason);
}
