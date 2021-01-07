package com.atguigu.guli.service.sms.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author hehao
 * @create 2020-12-30 17:57
 */

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsProperties {
    private String regionId; //: cn-hangzhou #地域ID
    private String accessKeyId; //: "LTAI4GEiESw7Vcys5W7XaRcT" #阿里云KeyId
    private String accessKeySecret; //: "eHBetznpl9EIeMxw4kFBOLfrqtq7UW" #阿里云KeySecret
    private String templateCode; //: SMS_205881616  #短信模板code
    private String signName; //: 美年旅游 #短信模板签名
}
