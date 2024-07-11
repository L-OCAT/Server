package com.locat.api.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class LocatApiException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;
    private final Integer code;

    protected LocatApiException(ApiExceptionType apiExceptionType) {
        super(apiExceptionType.getMessage());
        this.httpStatus = HttpStatus.resolve(apiExceptionType.getStatusCode());
        this.message = apiExceptionType.getMessage();
        this.code = apiExceptionType.getCode();
    }
}
