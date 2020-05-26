package com.c2olshare.registry.common.exception;

import com.c2olshare.registry.common.constant.ErrorCode;

/**
 * @author MaJing
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 5434407759965191828L;

    public BusinessException(ErrorCode code) {
        super(code.getMessage());
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
