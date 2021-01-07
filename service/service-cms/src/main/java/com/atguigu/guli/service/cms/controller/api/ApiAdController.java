package com.atguigu.guli.service.cms.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-29
 */
//@CrossOrigin  由gateway网关全局路由配置 //解决跨域问题
@Api(tags = "内容管理模块(用户)")
@RestController
@RequestMapping("/api/cms/ad")
public class ApiAdController {

    @Autowired
    private AdService adService;

    //1、根据typeId查询广告
    @ApiOperation(value = "根据typeId查询广告")
    @GetMapping(value = "/get-Ad/{typeId}")
    public R getAdByTypeId(
            @ApiParam(value = "广告类型ID", required = true) @PathVariable(value = "typeId") String typeId) {
        List<Ad> adList = adService.getAdByTypeId(typeId);
        return R.ok().message("获取广告数据成功").data("adList", adList);
    }
}

