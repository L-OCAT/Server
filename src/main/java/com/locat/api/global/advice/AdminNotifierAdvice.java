package com.locat.api.global.advice;

import com.locat.api.global.annotations.RequireAdminNotification;
import com.locat.api.global.utils.LocatSpelParser;
import com.locat.api.infrastructure.external.DiscordClient;
import com.locat.api.infrastructure.external.WebhookRequest;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * {@link RequireAdminNotification} 어노테이션이 붙은 메소드의 실행 결과에 따라 관리자에게 알림을 보내는 Advice <br>
 * 상용 환경({@code @Profile("prod")})에서만 동작하도록 설정되어 있습니다.
 */
@Aspect
@Component
@Profile("prod")
@RequiredArgsConstructor
public class AdminNotifierAdvice {

  @Value("${discord.webhook.server-id}")
  private String serverId;

  @Value("${discord.webhook.token}")
  private String webhookToken;

  private final DiscordClient discordClient;

  @Around("@annotation(requireAdminNotify)")
  public Object handleNotification(
      ProceedingJoinPoint joinPoint, RequireAdminNotification requireAdminNotify) throws Throwable {
    Object methodResult = joinPoint.proceed();

    if (shouldNotify(joinPoint, requireAdminNotify, methodResult)) {
      String message = requireAdminNotify.message();
      WebhookRequest request = new WebhookRequest(message);
      discordClient.send(serverId, webhookToken, request);
    }

    return methodResult;
  }

  private boolean shouldNotify(
      ProceedingJoinPoint joinPoint,
      RequireAdminNotification requireAdminNotify,
      Object methodResult) {
    String condition = requireAdminNotify.condition();
    return StringUtils.isBlank(condition) || evaluateCondition(condition, joinPoint, methodResult);
  }

  private boolean evaluateCondition(
      String expression, ProceedingJoinPoint joinPoint, Object methodResult) {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    return LocatSpelParser.evaluateExpression(
        expression, methodSignature.getParameterNames(), methodResult);
  }
}
