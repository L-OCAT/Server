package com.locat.api.domain.user;

import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.event.UserAuthenticatedEvent;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

  private final UserService userService;

  /**
   * 사용자 인증 완료 시 사용자 활성화 처리 이벤트 핸들러
   */
  @Async("asyncExcecutor")
  @EventListener(UserAuthenticatedEvent.class)
  public void handleUserAuthenticatedEvent(UserAuthenticatedEvent event) {
    final User user = this.userService.findByEmail(event.getUserEmail())
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
    user.activate();
  }
}
