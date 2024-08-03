package com.locat.api.global.auth.jwt;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;

public class TokenException extends LocatApiException {

    protected TokenException(ApiExceptionType apiExceptionType) {
        super(apiExceptionType);
    }
}
