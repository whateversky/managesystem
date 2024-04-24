package com.ebay.managesystem.enums;

public enum ErrorCodeEnums {
    SUCCESS(200, "success"),
    BUSINESS_ERROR(500, "server error");

    Integer code;
    String msg;

    ErrorCodeEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
