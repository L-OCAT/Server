package com.locat.api.infra.client.http;

import com.locat.api.domain.common.misc.WebhookRequest;
import com.locat.api.global.advice.RequireAdminNotification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "discordClient", url = "https://discord.com/api")
public interface DiscordClient {

  /**
   * Discord 웹훅 메시지 전송 <br>
   *
   * <p>{@link RequireAdminNotification}로 알림을 보낼 때 등에 사용
   *
   * @param serverId 서버 ID
   * @param webhookToken 웹훅 토큰
   * @param request 웹훅 요청
   */
  @PostMapping("/webhooks/{serverId}/{webhookToken}")
  void send(
      @PathVariable("serverId") String serverId,
      @PathVariable("webhookToken") String webhookToken,
      @RequestBody WebhookRequest request);
}
