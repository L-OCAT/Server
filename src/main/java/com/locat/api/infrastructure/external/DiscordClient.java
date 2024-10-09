package com.locat.api.infrastructure.external;

import com.locat.api.domain.common.misc.WebhookRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "discordClient", url = "https://discord.com/api")
public interface DiscordClient {

  @PostMapping("/webhooks/{serverId}/{webhookToken}")
  void send(
      @PathVariable("serverId") String serverId,
      @PathVariable("webhookToken") String webhookToken,
      @RequestBody WebhookRequest request);
}
