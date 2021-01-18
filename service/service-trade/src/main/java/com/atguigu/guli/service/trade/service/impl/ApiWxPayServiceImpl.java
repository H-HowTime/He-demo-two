package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.common.util.StreamUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.utils.HttpClientUtils;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.entity.PayLog;
import com.atguigu.guli.service.trade.entity.vo.PayVo;
import com.atguigu.guli.service.trade.feign.CourseFeignClient;
import com.atguigu.guli.service.trade.mapper.OrderMapper;
import com.atguigu.guli.service.trade.mapper.PayLogMapper;
import com.atguigu.guli.service.trade.service.ApiWxPayService;
import com.atguigu.guli.service.trade.utils.WxPayProperties;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hehao
 * @create 2021-01-05 19:30
 */
@Slf4j
@Service
public class ApiWxPayServiceImpl implements ApiWxPayService {

    @Autowired
    private WxPayProperties wxPayProperties;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PayLogMapper payLogMapper;

    @Autowired
    private CourseFeignClient courseFeignClient;

    @Override
    public PayVo createNative(String orderNo, HttpServletRequest request) {

        log.info("nacos配置中心微信支付：{}",wxPayProperties);

        try {
            //获取终端（客户端）IP
            String remoteHost = request.getRemoteHost();

            //通过orderNo获取用户信息
            QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.eq("order_no", orderNo);
            Order order = orderMapper.selectOne(orderQueryWrapper);
            System.out.println(order);
            //URL地址：https://api.mch.weixin.qq.com/pay/unifiedorder(微信统一下单接口)
            //使用httpclientUtils工具类发送post请求
            HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置HTTPS
            client.setHttps(true);
            //设置请求参数，以xml形式设置到请求体中
            Map<String, String> resParamsMap = new HashMap<>();
            resParamsMap.put("appid", wxPayProperties.getAppId()); //公众账号id
            resParamsMap.put("mch_id", wxPayProperties.getPartner()); //商户号
            resParamsMap.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串
            resParamsMap.put("body", order.getCourseTitle()); //商品描述
            resParamsMap.put("out_trade_no", orderNo); //商品订单号
            resParamsMap.put("total_fee", order.getTotalFee().toString()); //商品标价
            resParamsMap.put("spbill_create_ip", remoteHost); //终端IP
            resParamsMap.put("notify_url", wxPayProperties.getNotifyUrl()); //通知地址 用户支付成功后回调的地址，必须保证外网可以访问
            resParamsMap.put("trade_type", "NATIVE"); //交易类型 NATIVE -Native支付

            //将map类型的参数转换为xml格式的字符串，并根据商户key生成签名
            String signedXml = WXPayUtil.generateSignedXml(resParamsMap, wxPayProperties.getPartnerKey());
            log.info("xml格式字符串请求体参数：{}", signedXml);
            //将xml格式字符串的参数设置到请求体中
            client.setXmlParam(signedXml);
            //发送post请求
            client.post();
            //获取响应对象，也是xml格式的字符串
            String contentXml = client.getContent();
            log.info("xml格式字符串响应体参数：{}", contentXml);
            //验证响应数据是否合法
            boolean signatureValid = WXPayUtil.isSignatureValid(contentXml, wxPayProperties.getPartnerKey());
            if(!signatureValid){
                //数据和签名不一致
                log.error("数据和签名不一致");
                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }
            //将xml格式的字符串响应对象转换为map
            Map<String, String> contentXmlToMap = WXPayUtil.xmlToMap(contentXml);
            //获取响应状态
            String returnCode = contentXmlToMap.get("return_code");
            String resultCode = contentXmlToMap.get("result_code");
            if (!"SUCCESS".equals(returnCode) || !"SUCCESS".equals(resultCode)) {
                log.error("微信支付统一下单错误 - "
                        + "return_code: " + contentXmlToMap.get("return_code")
                        + "return_msg: " + contentXmlToMap.get("return_msg")
                        + "result_code: " + contentXmlToMap.get("result_code")
                        + "err_code: " + contentXmlToMap.get("err_code")
                        + "err_code_des: " + contentXmlToMap.get("err_code_des"));
                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }
            //封装PayVo数据
            PayVo payVo = new PayVo();
            payVo.setCodeUrl(contentXmlToMap.get("code_url"));
            payVo.setCourseId(order.getCourseId());
            payVo.setOrderNo(order.getOrderNo());
            payVo.setResultCode(resultCode);
            payVo.setTotalFee(order.getTotalFee());
            return payVo;
        } catch (Exception e) {
            log.error("统一下单异常：{}", e.getMessage());
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }
    }

    @Override
    public String callBackNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("支付成功后的回调");
            //支付成功后，微信会将支付信息以xml格式储存在请求体中
            //获取请求对像的流数据
            ServletInputStream inputStream = request.getInputStream();
            //将流数据转化为xml格式的字符串
            String resXml = StreamUtils.inputStream2String(inputStream, "UTF-8");
            log.info("通知结果：{}",resXml);
            Map<String,String> returnMap = new HashMap<>();
            //判断签名（判断数据是否合法）
            boolean valid = WXPayUtil.isSignatureValid(resXml, wxPayProperties.getPartnerKey());
            if(!valid){
                //数据和签名不一致
                log.error("数据和签名不一致");
                returnMap.put("return_code","FAIL");
                returnMap.put("return_msg","签名失败");
                String returnXml = WXPayUtil.mapToXml(returnMap);
                return returnXml;
//                throw new GuliException(ResultCodeEnum.PAY_SIGN_ERROR);
            }
            //将xml格式的字符串转化为map
            Map<String, String> resXmlToMap = WXPayUtil.xmlToMap(resXml);
            String returnCode = resXmlToMap.get("return_code");
            String resultCode = resXmlToMap.get("result_code");
            if(!"SUCCESS".equals(returnCode)){
//                throw new GuliException(ResultCodeEnum.PAY_ORDERQUERY_ERROR);
                returnMap.put("return_code","FAIL");
                returnMap.put("return_msg","微信支付业务失败");
                String returnXml = WXPayUtil.mapToXml(returnMap);
                return returnXml;
            }

            if(!"SUCCESS".equals(resultCode)){
//                throw new GuliException(ResultCodeEnum.PAY_ORDERQUERY_ERROR);
                returnMap.put("return_code","FAIL");
                returnMap.put("return_msg","微信支付业务失败");
                String returnXml = WXPayUtil.mapToXml(returnMap);
                return returnXml;
            }
            //判断支付金额和订单金额是否一致
            String totalFee = resXmlToMap.get("total_fee");
            String tradeNo = resXmlToMap.get("out_trade_no");
            QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.eq("order_no",tradeNo);
            Order order = orderMapper.selectOne(orderQueryWrapper);
            if(Long.parseLong(totalFee) != order.getTotalFee()){
                log.error("支付金额和订单金额是不一致");
//                throw new GuliException(ResultCodeEnum.PAY_FEE_UNLIKE);
                returnMap.put("return_code","FAIL");
                returnMap.put("return_msg","支付金额和订单金额是不一致");
                String returnXml = WXPayUtil.mapToXml(returnMap);
                return returnXml;
            }
            //用户支付成功，更新订单支付状态和商品的销量
            order.setStatus(1);
            order.setPayType(1); //微信支付
            orderMapper.updateById(order);

            //代码是同步执行的，更改销量和保存支付日志影响性能，以后可以放到rabbitMQ消息队列中，监控支付状态，
            // 当支付成功是在其他地方执行这两个操作，减少对当前应用的性能影响

            // 远程调用edu服务更改商品的销量
            String courseId = order.getCourseId();
            R buyCount = courseFeignClient.updateCourse2BuyCount(courseId);
            if (!buyCount.getSuccess()) {
                throw new GuliException(ResultCodeEnum.REMOTE_REQUEST_ERROR);
            }

            //保存回调信息到支付日志表中
            PayLog payLog = new PayLog();
            Gson gson = new Gson();
            payLog.setAttr(gson.toJson(resXmlToMap));
            payLog.setOrderNo(order.getOrderNo());
            payLog.setPayTime(new Date());
            payLog.setPayType(1);
            payLog.setTotalFee(order.getTotalFee());
            payLog.setTradeState("已完成");
            payLog.setTransactionId(resXmlToMap.get("transaction_id"));
            payLogMapper.insert(payLog);

            //支付完成成功响应
            returnMap.put("return_code","SUCCESS");
            returnMap.put("return_msg","OJBK");
            String returnXml = WXPayUtil.mapToXml(returnMap);
            return returnXml;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new GuliException(ResultCodeEnum.PAY_ORDERQUERY_ERROR);
        }

    }
}
