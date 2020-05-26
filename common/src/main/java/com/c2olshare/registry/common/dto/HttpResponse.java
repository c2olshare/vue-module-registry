package com.c2olshare.registry.common.dto;

import com.c2olshare.registry.common.constant.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author MaJing
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse<T> implements Serializable {

    private static final long serialVersionUID = 1994496862798737432L;

    private final String code;

    private final String message;

    private final T data;

    private HttpResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> HttpResponse<T> build(ErrorCode code) {
        return new HttpResponse<>(code.getCode(), code.getMessage(), null);
    }

    public static <T> HttpResponse<T> build(ErrorCode code, String message) {
        return new HttpResponse<>(code.getCode(), message, null);
    }

    public static <T> HttpResponse<T> build(ErrorCode code, String message, T data) {
        return new HttpResponse<>(code.getCode(), message, data);
    }

    public static <T> HttpResponse<T> build(String code, String message) {
        return new HttpResponse<>(code, message, null);
    }

    public static <T> HttpResponse<T> build(String code, String message, T data) {
        return new HttpResponse<>(code, message, data);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

}
