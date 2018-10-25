package com.xhinliang.birthdays.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 对外吐数据统一使用这个类
 */
public class NormalResponse<T> {

    private int code;
    private String message;
    private T data;

    @JsonCreator
    public NormalResponse() {
    }

    public NormalResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> NormalResponse<T> ofData(T data) {
        return new NormalResponse<>(0, "OK", data);
    }

    public static NormalResponse<Void> ofEmptyData() {
        return new NormalResponse<>(0, "OK", null);
    }

    public static NormalResponse<Void> ofThrowable(Throwable e) {
        return NormalResponse.ofError(1, e.getMessage());
    }

    public static NormalResponse<Void> ofError(int errorCode, String message) {
        return new NormalResponse<>(errorCode, message, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
