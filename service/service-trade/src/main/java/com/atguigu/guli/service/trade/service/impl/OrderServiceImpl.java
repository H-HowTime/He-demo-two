package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.common.util.OrderNoUtils;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.base.utils.JwtInfo;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.feign.CourseFeignClient;
import com.atguigu.guli.service.trade.feign.MemberFeignClient;
import com.atguigu.guli.service.trade.mapper.OrderMapper;
import com.atguigu.guli.service.trade.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2021-01-05
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private CourseFeignClient courseFeignClient;

    @Autowired
    private MemberFeignClient memberFeignClient;

    @Override
    public String createOrder(String courseId, HttpServletRequest request) {
        //获取用户信息id
        String memberId = JwtHelper.getId(request);
        System.out.println(memberId);
        //判断该用户是否已经购买过课程
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("course_id", courseId);
        orderQueryWrapper.eq("member_id", memberId);
        Order selectOne = baseMapper.selectOne(orderQueryWrapper);
        if (selectOne != null) {
            //用户已经购买过该课程,直接返回订单编号
            return selectOne.getOrderNo();
        }
        //远程调用获取课程信息
        R courseR = courseFeignClient.courseOrderById(courseId); //返回值为linkedHashMap,无法强制类型转换
        if (!courseR.getSuccess()) {
            throw new GuliException(ResultCodeEnum.REMOTE_REQUEST_ERROR);
        }
        Object courseDtoObj = courseR.getData().get("courseDto");
        System.out.println(courseDtoObj.getClass().getName());
        ObjectMapper objectMapper = new ObjectMapper(); //使用ObjectMapper进行类型转换
        CourseDto courseDto = objectMapper.convertValue(courseDtoObj, CourseDto.class);
        //远程调用获取会员信息
        R memberR = memberFeignClient.getMemberInfo(memberId);
        if (!memberR.getSuccess()) {
            throw new GuliException(ResultCodeEnum.REMOTE_REQUEST_ERROR);
        }
        Object memberDtoObj = memberR.getData().get("memberDto");
        MemberDto memberDto = objectMapper.convertValue(memberDtoObj, MemberDto.class);
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo()); //设置订单编号
        //会员信息
        order.setNickname(memberDto.getNickname()); //会员昵称
        order.setMobile(memberDto.getMobile()); //手机号码
        order.setMemberId(memberDto.getId()); //会员id
        //课程信息
        order.setCourseId(courseDto.getId()); //课程id
        order.setCourseTitle(courseDto.getTitle()); //课程标题
        order.setCourseCover(courseDto.getCover()); //课程封面
        order.setTotalFee(courseDto.getPrice().multiply(new BigDecimal(100)).longValue()); //课程价格（订单金额：分）
        //讲师信息
        order.setTeacherName(courseDto.getTeacherName()); //讲师姓名
        //支付状态
        order.setPayType(1); //微信支付
        order.setStatus(0); //未支付
        baseMapper.insert(order);
        log.warn("订单信息：{}", order);
        return order.getOrderNo();
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderNo);
//        orderQueryWrapper.select()
        return baseMapper.selectOne(orderQueryWrapper);
    }

    @Override
    public Integer isBuyOrder(String courseId, HttpServletRequest request) {
        //获取用户id
        String memberId = JwtHelper.getId(request);
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("member_id", memberId);
        orderQueryWrapper.eq("course_id", courseId);
        orderQueryWrapper.eq("status", 1);
        return baseMapper.selectCount(orderQueryWrapper);
    }


    @Override
    public List<Order> getOrdersByMember(String memberId,HttpServletRequest request) {
        String id = JwtHelper.getId(request);
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.orderByDesc("gmt_create");
        orderQueryWrapper.eq("member_id", id);
        List<Order> orders = baseMapper.selectList(orderQueryWrapper);
        return orders;
    }

    @Override
    public boolean deleteOrder(String memberId, String orderId) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("member_id", memberId);
        orderQueryWrapper.eq("id", orderId);
        int delete = baseMapper.delete(orderQueryWrapper);
        if (delete > 0) {
            return true;
        }
        return false;
    }
}
