package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.feign.fallback.CourseFeignClientFallBack;
import com.atguigu.guli.service.trade.feign.fallback.MemberFeignClientFallBack;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author hehao
 * @create 2021-01-05 15:03
 */
@Service
@FeignClient(value = "service-ucenter",fallback = MemberFeignClientFallBack.class) //指明远程调用的服务以及兜底数据
public interface MemberFeignClient {

    @ApiOperation(value = "订单服务远程调用获取会员信息")
    @GetMapping(value = "/api/ucenter/member/auth/get-member-info/{memberId}")
    public R getMemberInfo(
            @ApiParam(value = "会员id", required = true) @PathVariable(value = "memberId") String memberId);
}
