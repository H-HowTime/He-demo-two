package com.atguigu.guli.service.oss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hehao
 * @create 2020-12-20 15:45
 */

@ComponentScan(basePackages = "com.atguigu.guli.service")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//该微服务不需要数据库连接 取消数据源的自动配置
public class ServiceOSSApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOSSApplication.class,args);
    }
}
