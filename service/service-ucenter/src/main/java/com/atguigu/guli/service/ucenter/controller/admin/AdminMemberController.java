package com.atguigu.guli.service.ucenter.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/admin/ucenter/member")
public class AdminMemberController {

    @Autowired
    private MemberService memberService;

    //1、后台统计分析服务远程调用获取用户注册数量
    @ApiOperation(value = "后台统计分析服务远程调用获取用户注册数量")
    @GetMapping(value = "/get-memberNum/{dateDay}")
    public R getMemberNum(
            @ApiParam(value = "注册时间") @PathVariable(value = "dateDay") String dateDay){
        int memberNum = memberService.getMemberNum(dateDay);
        //select count(*) from ucenter_member where DATE(gmt_create) = '2019-01-19' 使用mysql的DATE方法可以将时间格式化为2019-01-19
        return R.ok().message("获取用户注册数量成功").data("memberNum",memberNum);
    }

}

