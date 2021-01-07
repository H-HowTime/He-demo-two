package com.atguigu.guli.service.cms.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.vo.AdVo;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-29
 */
////@CrossOrigin  由gateway网关全局路由配置  由gateway网关全局路由配置
@Api(tags = "内容管理模块")
@RestController
@RequestMapping("/admin/cms/ad")
public class AdminAdController {

    @Autowired
    private AdService adService;

    //1、新增一条广告
    @ApiOperation(value = "新增一条广告")
    @PostMapping(value = "/save-Ad")
    public R saveAd(
            @ApiParam(value = "广告数据") @RequestBody Ad ad) {
        boolean b = adService.save(ad);
        if (b) {
            return R.ok().message("新增广告成功");
        }
        return R.error().message("新增广告失败");
    }

    //2、删除一条广告
    @ApiOperation(value = "删除一条广告")
    @DeleteMapping(value = "/delete-Ad/{id}")
    public R deleteAdById(
            @ApiParam(value = "广告ID", required = true) @PathVariable(value = "id") String id) {
        //TODO 删除广告图片？
        boolean b = adService.removeById(id);
        if (b) {
            return R.ok().message("删除广告成功");
        }
        return R.error().message("删除广告失败");
    }

    //3、根据id获取指定广告
    @ApiOperation(value = "根据id获取指定广告")
    @GetMapping(value = "/get-Ad/{id}")
    public R getAdById(
            @ApiParam(value = "广告ID", required = true) @PathVariable(value = "id") String id) {
        Ad ad = adService.getById(id);
        return R.ok().message("获取广告成功").data("ad", ad);
    }

    //4、更新一条广告
    @ApiOperation(value = "更新一条广告")
    @PutMapping(value = "/update-Ad")
    public R updateAd(
            @ApiParam(value = "广告数据") @RequestBody Ad ad) {
        boolean b = adService.updateById(ad);
        if (b) {
            return R.ok().message("更新广告成功");
        }
        return R.error().message("更新广告失败");
    }

    //5、获取广告内容列表
    @ApiOperation(value = "获取广告内容")
    @GetMapping(value = "/get-Ad-list")
    public R getAdList() {
        List<Ad> adList = adService.list();
        return R.ok().message("获取广告列表成功").data("adList", adList);
    }

    //TODO 后台管理系统中前端对广告的增删改查
    //6、获取广告内容列表分页数据
    @ApiOperation(value = "获取广告内容分页数据")
    @GetMapping(value = "/get-Ad-page/{pageNum}/{pageSize}")
    public R getAdPage(
            @ApiParam(value = "当前页") @PathVariable(value = "pageNum") Long pageNum,
            @ApiParam(value = "每页包含的条数") @PathVariable(value = "pageSize") Long pageSize) {
        IPage<AdVo> page = adService.getAdPage(pageNum, pageSize);
        return R.ok().message("获取广告分页列表成功").data("page", page);
    }
}

