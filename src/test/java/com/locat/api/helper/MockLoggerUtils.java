package com.locat.api.helper;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.slf4j.LoggerFactory;

public final class MockLoggerUtils {

  private MockLoggerUtils() {}

  /**
   * 모킹된 Appender를 생성하고, 주어진 클래스의 Logger에 추가합니다.
   *
   * @param mockAppender 모킹된 Appender
   * @param targetClass 로거를 추가할 대상 클래스
   * @param level 로깅 레벨
   */
  public static void attachMockAppender(
      Appender<ILoggingEvent> mockAppender, Class<?> targetClass, Level level) {
    Logger logger = (Logger) LoggerFactory.getLogger(targetClass);
    logger.addAppender(mockAppender);
    logger.setLevel(level);
  }
}
