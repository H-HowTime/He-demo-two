package com.atguigu.guli.service.base.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author hehao
 * @create 2020-12-15 18:56
 */
@EnableTransactionManagement //启用声明式事务（通过注解就可以控制事务 ）
@MapperScan(basePackages = "com.atguigu.guli.service.*.mapper") //扫描指定包下的mapper接口
@Configuration //标识为配置类 将该组件扫描到容器中
public class MybatisPlusConfig {

    @Bean //mp乐观锁拦截器
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }

    @Bean //mp分页拦截器
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
