package com.zondy.jwt.jwtmobile.entity;

/**
 * Created by ywj on 2017/3/16 0016.
 */

public class EntityBaseResponse<T> {
    String message;
    int result;
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResult() {
        return result;
    }

    public void setResultCode(int result) {
        this.result = result;
    }
}
