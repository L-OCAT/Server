package com.locat.api.domain.admin.dto;

/**
 * 관리자용 대시보드 서비스 통계 정보 응답 DTO
 *
 * @param totalUsers LOCAT에 가입한 전체 사용자 수
 * @param totalLostItems 등록된 전체 분실물 수
 * @param totalFoundItems 등록된 전체 습득물 수
 */
public record AdminDashboardSummaryDto(int totalUsers, int totalLostItems, int totalFoundItems) {}
