package com.atguigu.guli.service.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.guli.common.util.FormUtils;
import com.atguigu.guli.common.util.RandomUtils;
import com.atguigu.guli.service.base.consts.ServiceConstants;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.sms.service.SmsService;
import com.atguigu.guli.service.sms.utils.SmsProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hehao
 * @create 2020-12-30 18:07
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SmsProperties smsProperties;

    @Override
    public void sendMessage(String mobile) {

        log.info("阿里云短信配置文件：{}",smsProperties);

        //1、验证手机号码格式是否正确
        boolean isMobile = FormUtils.isMobile(mobile);
        if(StringUtils.isEmpty(mobile) || !isMobile){
            //手机号码格式不正确
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }
        //2、判断Redis中是否有未过期的验证码
        String redisCodeKey = ServiceConstants.SMS_CODE_PREFIX + mobile;  //redis中储存验证码的key
        Object codeRedis = redisTemplate.opsForValue().get(redisCodeKey);
        if(!StringUtils.isEmpty(codeRedis)){
            //验证码已存在
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
        }
        //3、生成验证码
        String code = RandomUtils.getSixBitRandom();
        //4、调用阿里云api发送验证码

        DefaultProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(),
                smsProperties.getAccessKeyId(), smsProperties.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", smsProperties.getSignName());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());
        //动态传入验证码
        Map<String,String> codeMap = new HashMap<>();
        codeMap.put("code",code);
        Gson gson = new Gson();
        String codeJson = gson.toJson(codeMap);
        request.putQueryParameter("TemplateParam", codeJson);
        try {
            CommonResponse response = client.getCommonResponse(request);
            String data = response.getData();
            //将json字符串解析成map
            Map fromJson = gson.fromJson(data, Map.class);
            String status = fromJson.get("Code").toString();
            if(!"OK".equals(status)){
                log.error(data);
                //短息发送失败
                throw new RuntimeException();
            }
            //5、短信发送成功，将验证码保存在Redis中一份

            redisTemplate.opsForValue().set(redisCodeKey,code,3600l, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        }
        //6、--判断该手机号24小时内获取验证码的次数是否超过约定次数
        //7、--判断该手机号1分钟内发送验证码的次数是否超过约定次数（防止表单重复提交）
    }
}
