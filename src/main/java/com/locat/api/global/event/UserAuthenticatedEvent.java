package com.locat.api.global.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 사용자 인증 완료 이벤트 <br>
 * 이메일 인증 완료 등 사용자 가입 및 인증 절차 완료 시 Publish
 */
@Getter
public class UserAuthenticatedEvent extends ApplicationEvent {

  private final String userEmail;

  private UserAuthenticatedEvent(Object source, String userEmail) {
    super(source);
    this.userEmail = userEmail;
  }

  public static UserAuthenticatedEvent of(Object source, String userEmail) {
    return new UserAuthenticatedEvent(source, userEmail);
  }
}
