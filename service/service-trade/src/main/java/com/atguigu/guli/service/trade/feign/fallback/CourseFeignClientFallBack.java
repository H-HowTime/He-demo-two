package com.atguigu.guli.service.trade.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.feign.CourseFeignClient;
import org.springframework.stereotype.Service;

/**
 * @author hehao
 * @create 2021-01-05 15:09
 */
@Service
public class CourseFeignClientFallBack implements CourseFeignClient {

    @Override
    public R courseOrderById(String courseId) {

        return R.setResult(ResultCodeEnum.REMOTE_REQUEST_ERROR);
    }

    @Override
    public R updateCourse2BuyCount(String courseId) {

        return R.setResult(ResultCodeEnum.REMOTE_REQUEST_ERROR);
    }
}
