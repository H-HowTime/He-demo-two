package com.atguigu.guli.service.statistics.feign;

import com.atguigu.guli.service.base.result.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author hehao
 * @create 2021-01-06 17:37
 */
@Service
@FeignClient(value = "service-ucenter")
public interface MemberFeignClient {

    @ApiOperation(value = "获取用户注册数量")
    @GetMapping(value = "/admin/ucenter/member/get-memberNum/{dateDay}")
    public R getMemberNum(
            @ApiParam(value = "注册时间") @PathVariable(value = "dateDay") String dateDay);
}
