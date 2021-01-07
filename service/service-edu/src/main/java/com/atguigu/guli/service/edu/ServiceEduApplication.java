package com.atguigu.guli.service.edu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author hehao
 * @create 2020-12-15 18:47
 */
//@EnableSwagger2 //开启swagger2功能 在service-base的配置类中指定
@EnableFeignClients //开启feign客户端功能
@EnableDiscoveryClient //开启nacos注册服务中心客户端功能
@ComponentScan(basePackages = "com.atguigu.guli.service") //扫描其他工程和当前工程下的com.atguigu.guli.service下的组件
@SpringBootApplication //主启动类
public class ServiceEduApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceEduApplication.class, args);
    }
}
