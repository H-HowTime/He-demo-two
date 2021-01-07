package com.atguigu.guli.service.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hehao
 * @create 2020-12-29 20:32
 */
@EnableCaching  //开启springCache缓存管理功能
@ComponentScan(basePackages = "com.atguigu.guli.service")
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmsApplication.class,args);
    }
}
