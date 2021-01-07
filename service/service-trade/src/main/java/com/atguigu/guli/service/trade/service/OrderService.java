package com.atguigu.guli.service.trade.service;

import com.atguigu.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author atguigu
 * @since 2021-01-05
 */
public interface OrderService extends IService<Order> {

    String createOrder(String courseId, HttpServletRequest request);

    Order getOrderByOrderNo(String orderNo);

    Integer isBuyOrder(String courseId, HttpServletRequest request);

    boolean deleteOrder(String memberId, String orderId);

    List<Order> getOrdersByMember(String memberId, HttpServletRequest request);
}
