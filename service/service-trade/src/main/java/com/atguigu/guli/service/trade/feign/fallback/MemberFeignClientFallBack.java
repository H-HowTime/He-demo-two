package com.atguigu.guli.service.trade.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.feign.CourseFeignClient;
import com.atguigu.guli.service.trade.feign.MemberFeignClient;
import org.springframework.stereotype.Service;

/**
 * @author hehao
 * @create 2021-01-05 15:09
 */
@Service
public class MemberFeignClientFallBack implements MemberFeignClient {

    @Override
    public R getMemberInfo(String memberId) {
        return R.setResult(ResultCodeEnum.REMOTE_REQUEST_ERROR);
    }
}
