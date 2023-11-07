package com.example.demossm.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/8/15 11:27
 */
@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e){
        return e.getMessage();
    }
}
