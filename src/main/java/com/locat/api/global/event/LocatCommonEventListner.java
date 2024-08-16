package com.locat.api.global.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocatCommonEventListner {

  @EventListener(ApplicationReadyEvent.class)
  public void init(ApplicationReadyEvent ignored) {
    log.info("""
      \s
         _____ __________ _    ____________     __________  ____  ____    __________     __________      __
        / ___// ____/ __ \\ |  / / ____/ __ \\   / ____/ __ \\/ __ \\/ __ \\  /_  __/ __ \\   / ____/ __ \\    / /
        \\__ \\/ __/ / /_/ / | / / __/ / /_/ /  / / __/ / / / / / / / / /   / / / / / /  / / __/ / / /   / /\s
       ___/ / /___/ _, _/| |/ / /___/ _, _/  / /_/ / /_/ / /_/ / /_/ /   / / / /_/ /  / /_/ / /_/ /   /_/ \s
      /____/_____/_/ |_| |___/_____/_/ |_|   \\____/\\____/\\____/_____/   /_/  \\____/   \\____/\\____/   (_)  \s
    \s""");
    log.info("LOCAT API Server is fully operational and READY to serve!");
    log.info("=".repeat(70));
  }
}
