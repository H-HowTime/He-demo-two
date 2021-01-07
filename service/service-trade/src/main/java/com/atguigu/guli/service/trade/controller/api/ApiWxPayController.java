package com.atguigu.guli.service.trade.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.entity.vo.PayVo;
import com.atguigu.guli.service.trade.service.ApiWxPayService;
import com.atguigu.guli.service.trade.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hehao
 * @create 2021-01-05 19:26
 */
@RefreshScope //动态监控nacos配置中心属性的修改
@Api(tags = "微信支付模块")
@RestController
@RequestMapping("/api/trade/weixin-pay")
public class ApiWxPayController {

    @Autowired
    private ApiWxPayService apiWxPayService;

    @Autowired
    private OrderService orderService;

    //1、获取微信支付二维码
    @ApiOperation(value = "获取微信支付二维码")
    @PostMapping(value = "/auth/create-native/{orderNo}")
    public R createNative(
            @ApiParam(value = "订单编号") @PathVariable(value = "orderNo") String orderNo, HttpServletRequest request) {
        PayVo payVo = apiWxPayService.createNative(orderNo, request);
        return R.ok().message("获取二维码成功").data("payVo", payVo);
    }

    //2、微信支付成功之后回调的函数
    @ApiOperation(value = "获取微信支付二维码")
    @PostMapping(value = "/callback/notify")
    public String callBackNotify(
            HttpServletRequest request, HttpServletResponse response) {
        String returnCode = apiWxPayService.callBackNotify(request, response);
        return returnCode;
    }

    //3、获取订单支付状态
    @ApiOperation(value = "获取订单支付状态")
    @GetMapping(value = "/auth/get-order-status/{orderNo}")
    public R getOrderStatus(
            @ApiParam(value = "订单编号") @PathVariable(value = "orderNo") String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(queryWrapper);
        return R.ok().message("获取订单状态成功").data("order", order);
    }
}
