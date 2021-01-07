package com.atguigu.guli.service.sms.controller.api;

import com.atguigu.guli.service.base.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hehao
 * @create 2021-01-06 20:33
 */
@RefreshScope //动态监听nacos配置中心属性的修改
@Api(tags = "测试nacos配置中心")
@RestController
@RequestMapping(value = "/api/sms/sample")
public class ApiSampleController {


    @Value(value = "${myData}")
    String myData;

    @Value(value = "${sms.redis}")
    String redis;

    @Value(value = "${one.group}")
    String group;

    //测试nacos配置中心
    @ApiOperation(value = "测试nacos配置中心")
    @GetMapping(value = "/test-nacos")
    public R testNacos() {
        System.out.println("配置中心myData:" + myData);
        System.out.println("配置中心redis:" + redis);
        System.out.println("配置中心group:" + group);
        return R.ok().message("测试方法执行成功");
    }
}
