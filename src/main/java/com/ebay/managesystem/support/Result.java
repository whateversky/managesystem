package com.ebay.managesystem.support;

import com.ebay.managesystem.enums.ErrorCodeEnums;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public static Result ok() {
        Result result = new Result();
        result.setCode(ErrorCodeEnums.SUCCESS.getCode());
        result.setMsg(ErrorCodeEnums.SUCCESS.getMsg());
        return result;
    }

    public static <T> Result ok(T t) {
        Result result = new Result();
        result.setCode(ErrorCodeEnums.SUCCESS.getCode());
        result.setMsg(ErrorCodeEnums.SUCCESS.getMsg());
        result.setData(t);
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result();
        result.setCode(ErrorCodeEnums.BUSINESS_ERROR.getCode());
        result.setMsg(msg);
        return result;
    }
}