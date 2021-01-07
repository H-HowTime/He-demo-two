package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.ucenter.service.ApiWxService;
import com.atguigu.guli.service.ucenter.utils.WxProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @author hehao
 * @create 2021-01-02 16:16
 */
@Slf4j
//@CrossOrigin  由gateway网关全局路由配置
@Controller  //有些方法需要重定向
@Api(tags = "微信登录管理模块")
@RequestMapping(value = "/api/ucenter/wx")
public class ApiWxController {

    @Autowired
    private ApiWxService apiWxService;

    //1、获取微信登录二维码
    @ApiOperation(value = "获取微信登录二维码")
    @GetMapping(value = "/login")
    public String genQrConnect(HttpSession session) {

        String qrCodeUrl = apiWxService.genQrConnect(session);

        return "redirect:" + qrCodeUrl; //重定向
    }

    //2、接收微信回调，获取用户信息
    @ApiOperation(value = "接收微信回调，获取用户信息")
    @GetMapping(value = "/callback")
    public String callback(String code, String state, HttpSession session) {

        System.out.println(code);
        System.out.println(state);

        String jwtStr = apiWxService.callback(code, state, session);

        return "redirect:http://localhost:3333?jwt=" + jwtStr;

    }
}
