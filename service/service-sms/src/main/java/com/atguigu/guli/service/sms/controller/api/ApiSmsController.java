package com.atguigu.guli.service.sms.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author hehao
 * @create 2020-12-30 18:04
 */
@Api(tags = "短信管理模块(用户)")
//@CrossOrigin  由gateway网关全局路由配置 //解决跨云问题
@RestController
@RequestMapping(value = "/api/sms")
public class ApiSmsController {

    @Autowired
    private SmsService smsService;

    //1、发送短信验证码
    @ApiOperation(value = "发送短信验证码")
    @GetMapping(value = "/send-message/{mobile}")
    public R sendMessage(
            @ApiParam(value = "手机号码",required = true) @PathVariable(value = "mobile") String mobile){
        smsService.sendMessage(mobile);
        return R.ok().message("短信验证码发送成功");
    }
}
