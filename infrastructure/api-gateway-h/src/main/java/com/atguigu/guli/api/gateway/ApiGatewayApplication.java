package com.atguigu.guli.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author hehao
 * @create 2021-01-04 18:57
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class,args);
    }
    /**
     * 在网关项目中使用filter
     * 第一种：网关项目已经写好的filter，只需要注入到容器中，会自动起作用
     * 第二种：自定义的全局filter（doFilter需要自己实现时业务），然后注入到容器中，会自动实现作用
     * 第三种：自定义的局部filter（doFilter需要自己实现时业务），然后去yml配置文件中使用filter -自定义filter 使用
     */
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");  //任意方法 允许的http方法
        config.addAllowedOrigin("*"); //任意来源 允许的请求源
        config.addAllowedHeader("*"); //任意请求头 允许的请求头
        config.setAllowCredentials(true); //任意签名  是否允许携带cookies
        source.registerCorsConfiguration("/**",config);
        return new CorsWebFilter(source);
    }
}
