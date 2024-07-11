package com.locat.api.global.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 관리자에게 알림을 보내는 이벤트
 */
@Getter
public class AdminNotifierEvent extends ApplicationEvent {

    private final String message;

    public AdminNotifierEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
}
