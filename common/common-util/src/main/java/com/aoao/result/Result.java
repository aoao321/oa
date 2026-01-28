package com.aoao.result;

import com.aoao.enums.ResponseCodeEnum;
import lombok.Data;

import java.io.Serializable;
@Data
public class Result<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String message; //错误信息
    private T data; //数据

    public static <T> Result<T> ok() {
        Result<T> result = new Result<T>();
        result.code = 200;
        return result;
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<T>();
        result.data = data;
        result.code = 200;
        return result;
    }

    public static <T> Result<T> fail(String msg) {
        Result result = new Result();
        result.message = msg;
        result.code = 201;
        return result;
    }

    public static <T> Result<T> fail(ResponseCodeEnum codeEnum) {
        Result result = new Result();
        result.message = codeEnum.getErrorMessage();
        result.code = codeEnum.getErrorCode();
        return result;
    }

}