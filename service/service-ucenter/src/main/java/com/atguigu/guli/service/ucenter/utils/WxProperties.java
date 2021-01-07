package com.atguigu.guli.service.ucenter.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hehao
 * @create 2021-01-02 16:17
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx.open")
public class WxProperties {

    private String appId; //:# 微信开放平台 appid  wxed9954c01bb89b47

    private String appSecret;//:# 微信开放平台 appsecret a7482517235173ddb4083788de60b90e

    private String redirectUri;//: # 微信开放平台 重定向url（guli.shop需要在微信开放平台配置） http://guli.shop/api/ucenter/wx/callback8160
}
