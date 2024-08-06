package com.locat.api.global.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locat.api.domain.core.ErrorResponse;
import com.locat.api.global.exception.LocatApiException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환을 위한 ObjectMapper

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response); // 다음 필터로 요청을 전달
        } catch (LocatApiException ex) {
            handleException(response, ex); // LocatApiException 발생 시 처리
        }
    }

    private void handleException(HttpServletResponse response, LocatApiException ex) throws IOException {
        response.setStatus(ex.getHttpStatus().value()); // 예외의 HTTP 상태 코드 설정
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorResponse errorResponse = ErrorResponse.fromException(ex); // ErrorResponse 객체 생성
        objectMapper.writeValue(response.getWriter(), errorResponse); // JSON 응답 작성
    }
}
