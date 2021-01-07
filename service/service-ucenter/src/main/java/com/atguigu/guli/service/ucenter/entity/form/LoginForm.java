package com.atguigu.guli.service.ucenter.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hehao
 * @create 2021-01-01 23:10
 */
@Data
@ApiModel(value="LoginForm对象", description="用户登录时提交的form表单数据封装")
public class LoginForm {

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;
}
