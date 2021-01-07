package com.atguigu.guli.service.statistics.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.statistics.feign.MemberFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hehao
 * @create 2021-01-06 17:41
 */
@Slf4j
@Service
public class MemberFeignClientFallBack implements MemberFeignClient {
    @Override
    public R getMemberNum(String dateDay) {
        log.error("远程调用会员微服务失败");
        return R.ok().data("memberNum",0);
    }
}
