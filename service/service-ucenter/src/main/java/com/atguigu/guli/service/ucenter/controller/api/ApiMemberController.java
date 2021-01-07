package com.atguigu.guli.service.ucenter.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.base.utils.JwtInfo;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.ucenter.entity.form.LoginForm;
import com.atguigu.guli.service.ucenter.entity.form.RegisterForm;
import com.atguigu.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-30
 */
@Api(tags = "用户中心模块")
//@CrossOrigin  由gateway网关全局路由配置 //解决跨域问题
@RestController
@RequestMapping("/api/ucenter/member")
public class ApiMemberController {

    @Autowired
    private MemberService memberService;

    //1、用户注册
    @ApiOperation(value = "用户注册")
    @PostMapping(value = "/register")
    public R register(
            @ApiParam(value = "用户信息", required = true) @RequestBody RegisterForm registerForm) {
        memberService.register(registerForm);
        return R.ok().message("注册成功");
    }

    //2、认证，用户登录
    @ApiOperation(value = "用户登录")
    @PostMapping(value = "/login")
    public R login(
            @ApiParam(value = "用户信息", required = true) @RequestBody LoginForm loginForm) {
        String jwtStr = memberService.login(loginForm);
        return R.ok().message("登录成功").data("jwtStr", jwtStr);
    }

    //3、解析token 获取用户登录信息
    @ApiOperation(value = "获取用户登录信息")
    @GetMapping(value = "/auth/get-user-info")
    public R getUserInfo(HttpServletRequest request) {
        //获取请求头中的token,解析token，获取用户登录信息
        JwtInfo jwtInfo = JwtHelper.getJwtInfo(request);
        return R.ok().data("jwtInfo", jwtInfo);
    }
    //4、测试session
    @ApiOperation(value = "设置session域中的值")
    @GetMapping(value = "/set-session")
    public R setSession(HttpSession session) {
        session.setAttribute("sKey", "sValue");
        return R.ok().message("设置session成功");
    }

    @ApiOperation(value = "获取session域中的值")
    @GetMapping(value = "/get-session")
    public R getSession(HttpSession session) {
        Object sKey = session.getAttribute("sKey");
        System.out.println(sKey);
        return R.ok().message("获取session成功").data("sKey", sKey);
    }
    //订单服务远程调用获取会员信息
    @ApiOperation(value = "订单服务远程调用获取会员信息")
    @GetMapping(value = "/auth/get-member-info/{memberId}")
    public R getMemberInfo(
            @ApiParam(value = "会员id", required = true) @PathVariable(value = "memberId") String memberId) {
       MemberDto memberDto =  memberService.getMemberInfo(memberId);
        return R.ok().message("订单服务远程调用获取会员信息成功").data("memberDto", memberDto);
    }
}

