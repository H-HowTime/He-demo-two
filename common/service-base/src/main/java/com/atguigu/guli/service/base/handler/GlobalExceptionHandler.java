package com.atguigu.guli.service.base.handler;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * @author hehao
 * @create 2020-12-16 9:55
 */
@RestControllerAdvice  //增强controller 配合@ExceptionHandler设置全局异常处理 精确匹配异常类型--大范围类型
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public R serviceEduException(Exception e){
        System.out.println("Exception异常处理方法被调用");
        e.printStackTrace();
        return R.error();
    }

    @ExceptionHandler(value = BadSqlGrammarException.class)
    public R sqlException(BadSqlGrammarException sql){
        System.out.println("BadSqlGrammarException异常处理方法被调用");
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public R jsonException(HttpMessageNotReadableException httpEx){
        System.out.println("HttpMessageNotReadableException异常处理方法被调用");
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }

    @ExceptionHandler(value = GuliException.class)
    public R guliException(GuliException guliEx ){
        System.out.println("GuliException异常处理方法被调用");
        guliEx.printStackTrace();
        return R.error().message(guliEx.getMessage());
    }
}
