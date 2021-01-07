package com.atguigu.guli.service.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hehao
 * @create 2020-12-27 17:59
 */
@EnableDiscoveryClient //开启nacos客户端功能
@ComponentScan(basePackages = "com.atguigu.guli.service")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //过滤掉数据库的自动配置
public class ServiceVodApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceVodApplication.class, args);
    }
}
