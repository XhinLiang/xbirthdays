package com.xhinliang.birthdays.common.exception;

import com.xhinliang.birthdays.common.constant.ErrorCode;

/**
 * @author xhinliang
 */
public class ServerException extends RuntimeException {

    public ServerException() {
    }

    public ServerException(ErrorCode errorCode) {
        this(errorCode.getMessage());
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }

    public ServerException(String message, Throwable cause, boolean enableSuppression,
                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
