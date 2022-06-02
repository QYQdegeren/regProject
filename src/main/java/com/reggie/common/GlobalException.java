package com.reggie.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})//拦截设定注解的类
@ResponseBody//可以把返回值封装成json返回
@Slf4j
public class GlobalException {

    /**
     * 进行异常处理，处理SQLIntegrityConstraintViolationException异常--sql的异常
     *
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            //如果异常提示消息有Duplicate entry，证明账户重复了
            String[] split = ex.getMessage().split(" ");
            String msg = split[2]+"已存在";
            return R.error(msg);
        }
        return R.error("error");
    }

    /**
     * 业务异常的处理
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> CustomexceptionHandler(CustomException ex){
        log.error(ex.getMessage());


        return R.error(ex.getMessage());
    }
}
