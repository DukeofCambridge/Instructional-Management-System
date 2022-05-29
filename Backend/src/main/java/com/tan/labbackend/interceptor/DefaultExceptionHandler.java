package com.tan.labbackend.interceptor;

import com.tan.labbackend.result.Result;
import com.tan.labbackend.result.ResultCode;
import com.tan.labbackend.result.ResultFactory;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class DefaultExceptionHandler {



    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleAuthorizationException(UnauthorizedException e) {
        String message = "权限认证失败";
        return ResultFactory.buildFailResult(message);
    }

    /**
     * @Valid 参数抛出错误
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public  Result  argumentValidException(MethodArgumentNotValidException e){
        return ResultFactory.buildResult(ResultCode.FAIL,e.getMessage(),null);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseBody
    public  Result  emptyResultDataAccessException(MethodArgumentNotValidException e){
        return ResultFactory.buildResult(ResultCode.FAIL,e.getMessage(),null);
    }
}
