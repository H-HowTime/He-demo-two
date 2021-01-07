package com.atguigu.guli.service.trade.service;

import com.atguigu.guli.service.trade.entity.vo.PayVo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hehao
 * @create 2021-01-05 19:30
 */
@Service
public interface ApiWxPayService {
    PayVo createNative(String orderNo, HttpServletRequest request);

    String callBackNotify(HttpServletRequest request, HttpServletResponse response);
}
