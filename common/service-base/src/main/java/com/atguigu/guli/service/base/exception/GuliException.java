package com.atguigu.guli.service.base.exception;

import com.atguigu.guli.service.base.result.ResultCodeEnum;
import lombok.Data;

/**
 * @author hehao
 * @create 2020-12-21 9:58
 */
@Data
public class GuliException extends RuntimeException{
    private Boolean success; //是否成功
    private Integer code; //响应状态码
    private String message; //错误信息

    public GuliException(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
    public GuliException(ResultCodeEnum resultCodeEnum){
//        super(resultCodeEnum.getMessage());
        this.success = resultCodeEnum.getSuccess();
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }
}
