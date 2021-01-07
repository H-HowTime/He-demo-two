package com.atguigu.guli.service.trade.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.awt.SunHints;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2021-01-05
 */
@Api(tags = "订单管理模块")
@RestController
@RequestMapping("/api/trade/order")
public class ApiOrderController {

    @Autowired
    private OrderService orderService;

    //1、创建订单
    @ApiOperation(value = "创建订单")
    @PostMapping(value = "/auth/create-order/{courseId}")
    public R createOrder(
            @ApiParam(value = "课程id", required = true) @PathVariable(value = "courseId") String courseId, @ApiParam(value = "请求对象") HttpServletRequest request) {
        String orderNo = orderService.createOrder(courseId, request);
        return R.ok().message("创建订单成功").data("orderNo", orderNo);
    }

    //2、根据订单编号获取订单
    @ApiOperation(value = "根据订单编号获取订单")
    @GetMapping(value = "/auth/get-order/{orderNo}")
    public R getOrderByOrderNo(
            @ApiParam(value = "订单编号", required = true) @PathVariable(value = "orderNo") String orderNo) {
        Order order = orderService.getOrderByOrderNo(orderNo);
        return R.ok().message("获取订单成功").data("order", order);
    }

    //3、判断用户是否已经购买过此课程
    @ApiOperation(value = "判断用户是否已经购买过此课程")
    @GetMapping(value = "/auth/is-buy-order/{courseId}")
    public R isBuyOrder(
            @ApiParam(value = "课程id", required = true) @PathVariable(value = "courseId") String courseId,
            @ApiParam(value = "请求对象") HttpServletRequest request) {
        int count = orderService.isBuyOrder(courseId, request);
        // 前端课时节点判断视频是否免费(已完成)
        return R.ok().message("获取订单成功").data("isBuy", count > 0);
    }

    //4、根据会员id获取订单列表
    @ApiOperation(value = "根据会员id获取订单列表")
    @GetMapping(value = "/auth/get-orders-member/{memberId}")
    public R getOrdersByMember(
            @ApiParam(value = "会员编号", required = true) @PathVariable(value = "memberId") String memberId,HttpServletRequest request) {
        List<Order> orders = orderService.getOrdersByMember(memberId,request);
        return R.ok().message("根据会员id获取订单列表成功").data("orders", orders);
    }

    //5、根据会员id和订单id删除订单
    @ApiOperation(value = "根据会员id和订单id删除订单")
    @DeleteMapping(value = "/auth/delete-order/{memberId}/{orderId}")
    public R deleteOrder(
            @ApiParam(value = "会员id", required = true) @PathVariable(value = "memberId") String memberId,
            @ApiParam(value = "订单id", required = true) @PathVariable(value = "orderId") String orderId) {
        boolean isDelete = orderService.deleteOrder(memberId, orderId);
        if (isDelete) {
            return R.ok().message("根据会员id和订单id删除订单成功");
        } else {
            return R.error().message("根据会员id和订单id删除订单失败");
        }

    }
}

