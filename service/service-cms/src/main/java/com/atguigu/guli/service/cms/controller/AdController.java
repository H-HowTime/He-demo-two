package com.atguigu.guli.service.cms.controller;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-29
 */
@Api(tags = "测试Redis")
@RestController
@RequestMapping("/cms/ad")
public class AdController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "测试Redis")
    @GetMapping(value = "/set")
    public R setRedis() {
//        redisTemplate.opsForValue().set("k1", "v1");
//        stringRedisTemplate.opsForValue().set("k2", "v2");
        redisTemplate.opsForValue().set("ad", new Ad());
        Ad ad = new Ad();
        Gson gson = new Gson();
        String toJson = gson.toJson(ad);
        stringRedisTemplate.opsForValue().set("ad1", toJson);

        return R.ok();
    }

    @ApiOperation(value = "测试Redis")
    @GetMapping(value = "get")
    public R getRedis() {
        Object k1 = redisTemplate.opsForValue().get("ad");
        String k2 = stringRedisTemplate.opsForValue().get("ad1");
        Gson gson = new Gson();
        Ad ad = gson.fromJson(k2, Ad.class);
        System.out.println(k1);
        System.out.println(ad);
        return com.atguigu.guli.service.base.result.R.ok();
    }
}

