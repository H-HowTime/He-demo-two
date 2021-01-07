package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.feign.fallback.CourseFeignClientFallBack;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author hehao
 * @create 2021-01-05 15:03
 */
@Service
@FeignClient(value = "service-edu", fallback = CourseFeignClientFallBack.class) //指明远程调用的服务以及兜底数据
public interface CourseFeignClient {

    @ApiOperation(value = "订单服务远程调用获取课程信息")
    @GetMapping(value = "/api/edu/course/course-order/{courseId}")
    public R courseOrderById(
            @ApiParam(value = "课程id") @PathVariable(value = "courseId") String courseId);

    @ApiOperation(value = "订单服务远程调用更改商品销量")
    @PutMapping(value = "/api/edu/course/update-course-buy-count/{courseId}")
    public R updateCourse2BuyCount(
            @ApiParam(value = "课程ID") @PathVariable(value = "courseId") String courseId);
}
