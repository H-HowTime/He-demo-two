package com.atguigu.guli.api.gateway.filter;

import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.base.utils.JwtInfo;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author hehao
 * @create 2021-01-04 21:19
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //鉴权，当用户访问需要鉴权的接口时，进行鉴权，其他页面直接放行
        //如果请求路径中有/api/**/auth/** 本次请求需要鉴权
        //1、获取请求路径
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        System.out.println("path = " + path);
        //2、使用请求路径和ant风格的路径/api/**/auth/** 进行比较，判断是否满足此规则 //TODO ant风格的url路径？
        AntPathMatcher matcher = new AntPathMatcher();
        boolean match = matcher.match("/api/**/auth/**", path);
        if (!match) {
            //当前路径不用鉴权，直接放行
            return chain.filter(exchange);
        }
        //3、进行鉴权
        //3.1获取jwt，判断用户是否登录
        String token = request.getHeaders().getFirst("token");
        boolean checkToken = JwtHelper.checkToken(token);
        if(checkToken){
            //鉴权通过，直接放行
            return chain.filter(exchange);
        }
        //4、鉴权失败，返回R.error()，给出错误的提示
        return responseData(response);
    }

    //如果有多个全局filter，执行顺序，数值越小，优先级越高
    @Override
    public int getOrder() {
        return 0;
    }

    //鉴权失败返回的Mono对象（封装失败的相关信息）
    public Mono<Void> responseData(ServerHttpResponse response){
        //TODO JsonObject对象DataBuffer对象moon对象
        //准备响应对象R装换为json字符串设置到响应体的响应报文中
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", ResultCodeEnum.LOGIN_AUTH.getSuccess());
        jsonObject.addProperty("message", ResultCodeEnum.LOGIN_AUTH.getMessage());
        jsonObject.addProperty("code", ResultCodeEnum.LOGIN_AUTH.getCode());
        jsonObject.addProperty("data", "");
        //将jsonObject对象装换为字节数组  jsonObject.toString()响应对象json字符串
        byte[] bytes = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
        //将字节数组转化为DataBuffer
        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
        //指定返回的数据类型并指定编码方式
        response.getHeaders().add("Content-Type","application/json;charset=UTF-8");
        //http响应
        return response.writeWith(Mono.just(dataBuffer));
    }
}
