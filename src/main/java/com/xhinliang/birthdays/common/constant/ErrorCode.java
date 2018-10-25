package com.xhinliang.birthdays.common.constant;

/**
 * @author xhinliang
 */
public enum ErrorCode {
    PARAM_INVALID(22), //
    ;

    private int code;
    private String message;

    ErrorCode(int code) {
        this.code = code;
    }

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
