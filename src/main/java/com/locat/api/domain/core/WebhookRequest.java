package com.locat.api.domain.core;

/**
 * Discord Webhook 요청 DTO
 *
 * @param content 메시지 내용
 */
public record WebhookRequest(String content) {}
