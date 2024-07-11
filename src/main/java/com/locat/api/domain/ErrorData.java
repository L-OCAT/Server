package com.locat.api.domain;

/**
 * 클라이언트에게 전달할 메세지, 코드를 포함하는 record
 *
 * @param message 메세지
 * @param code    코드
 */
public record ErrorData(String message, Integer code) {

    public static ErrorData of(String message, Integer code) {
        return new ErrorData(message, code);
    }

}
