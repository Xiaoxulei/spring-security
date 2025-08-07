package com.xuxiaolei.springsecurityjwt.common;

import lombok.Data;
//Result 返回结构
@Data
public class Result<T> {

    private boolean success;
    private Integer code;
    private String message;
    private T data;

    private Result() {}

    // 成功
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage("登录成功");
        r.setData(data);
        return r;
    }
    public static <T> Result<T> result(String message) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage(message);
        return r;
    }
    public static <T> Result<T> error(String message) {
        Result<T> r = new Result<>();
        r.setSuccess(false);
        r.setCode(403);
        r.setMessage(message);
        return r;
    }
    // 成功
    public static <T> Result<T> logout(T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage("注销成功");
        r.setData(data);
        return r;
    }

    // 失败
    public static <T> Result<T> fail(String message) {
        Result<T> r = new Result<>();
        r.setSuccess(false);
        r.setCode(401);
        r.setMessage(message);
        r.setData(null);
        return r;
    }
}
