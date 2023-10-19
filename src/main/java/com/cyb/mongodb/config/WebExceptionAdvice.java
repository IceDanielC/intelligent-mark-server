package com.cyb.mongodb.config;

import com.cyb.mongodb.dto.ExpireException;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.dto.UnloginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//全局错误处理
@Slf4j
@RestControllerAdvice
public class WebExceptionAdvice {

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error(e.toString(), e);
        return Result.fail("服务器异常");
    }

    @ExceptionHandler(UnloginException.class)
    public Result handleUnloginException(UnloginException e){
        return Result.fail(401,"未登录！");
    }

    @ExceptionHandler(ExpireException.class)
    public Result handleExpireException(ExpireException e){
        return Result.fail(401,"token已过期");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handlerLogin(){
        return Result.fail(401,"用户名密码不能为空！");
    }
}
