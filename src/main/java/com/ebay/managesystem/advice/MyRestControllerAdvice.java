package com.ebay.managesystem.advice;

import com.ebay.managesystem.enums.ErrorCodeEnums;
import com.ebay.managesystem.exception.ResourceAccessException;
import com.ebay.managesystem.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class MyRestControllerAdvice {
    @ExceptionHandler({ResourceAccessException.class})
    public Result handleResourceAccessException(ResourceAccessException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("", e);
        return Result.error(ErrorCodeEnums.BUSINESS_ERROR.getMsg());
    }

}
