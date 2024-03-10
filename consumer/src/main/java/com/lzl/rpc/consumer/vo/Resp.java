package com.lzl.rpc.consumer.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resp<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    public static <T> Resp<T> of(int code, String msg, T data) {
        Resp<T> resp = new Resp<>();
        resp.code = code;
        resp.msg = msg;
        resp.data = data;
        return resp;
    }

    public static <T> Resp<T> success(T data) {
        return of(SUCCESS, "操作成功", data);
    }

    public static <T> Resp<T> success() {
        return of(SUCCESS, "操作成功", null);
    }


    private static final long serialVersionUID = 1L;
    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
    public static final int NO_LOGIN = 2;
}
