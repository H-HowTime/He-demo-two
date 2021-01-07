package com.atguigu.guli.service.edu.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hehao
 * @create 2020-12-21 20:04
 */
@Configuration //标识该类为一个配置类，并扫进容器中
public class OpenfeignConfig {

    //

    /**
     * OpenFeign提供了日志打印功能，我们可以通过配置来调整日志级别，从而了解OpenFeign中Http请求的细节。
     * 即对OpenFeign远程接口调用的情况进行监控和日志输出。默认以debug级别打印日志  但我们设置全局统一日志，级别为info
     * NONE：默认级别，不显示日志
     * BASIC：仅记录请求方法、URL、响应状态及执行时间
     * HEADERS：除了BASIC中定义的信息之外，还有请求和响应头信息
     * FULL：除了HEADERS中定义的信息之外，还有请求和响应正文及元数据信息
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
