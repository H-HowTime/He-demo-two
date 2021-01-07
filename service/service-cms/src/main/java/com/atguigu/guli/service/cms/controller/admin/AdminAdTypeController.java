package com.atguigu.guli.service.cms.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.AdType;
import com.atguigu.guli.service.cms.entity.vo.AdVo;
import com.atguigu.guli.service.cms.mapper.AdTypeMapper;
import com.atguigu.guli.service.cms.service.AdTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 推荐位 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-29
 */
//@CrossOrigin  由gateway网关全局路由配置 //解决跨域问题
@Api(tags = "内容管理模块(广告类型)")
@RestController
@RequestMapping("/admin/cms/ad-type")
public class AdminAdTypeController {

    @Autowired
    private AdTypeService adTypeService;

    //1、获取所有推荐类别列表
    @ApiOperation(value = "获取所有推荐类别列表")
    @GetMapping(value = "/get-ad-type-list")
    public R getAdTypeList(){
        List<AdType> adTypeList = adTypeService.list();
        return R.ok().message("获取所有推荐类别列表成功").data("adTypeList",adTypeList);
    }

    //2、根据id删除推荐类别
    @ApiOperation(value = "根据id删除推荐类别")
    @DeleteMapping(value = "/delete-ad-type/{id}")
    public R deleteAdTypeById(
            @ApiParam(value = "推荐位id",required = true) @PathVariable(value = "id") String id){
        boolean b = adTypeService.removeById(id);
        if(b){
            return R.ok().message("删除推荐类别成功");
        }
        return R.error().message("删除推荐类别失败");
    }
    //3、根据id获取推荐类别
    @ApiOperation(value = "根据id获取推荐类别")
    @GetMapping(value = "/get-ad-type/{id}")
    public R getAdTypeById(
            @ApiParam(value = "推荐位id",required = true) @PathVariable(value = "id") String id){
        AdType adType = adTypeService.getById(id);
        if(adType != null){
            return R.ok().message("获取推荐类别成功").data("adType",adType);
        }
        return R.error().message("获取推荐类别失败");
    }
    //4、更新推荐类别
    @ApiOperation(value = "更新推荐类别")
    @PutMapping(value = "/update-ad-type")
    public R updateAdTypeById(
            @ApiParam(value = "推荐类型数据",required = true) @RequestBody AdType adType){
        boolean b = adTypeService.updateById(adType);
        if(b){
            return R.ok().message("更新推荐类别成功");
        }
        return R.error().message("更新推荐类别失败");
    }
    //5、新增推荐类别
    @ApiOperation(value = "新增推荐类别")
    @PostMapping(value = "/save-ad-type")
    public R saveAdTypeById(
            @ApiParam(value = "推荐类型数据",required = true) @RequestBody AdType adType){
        boolean b = adTypeService.save(adType);
        if(b){
            return R.ok().message("新增推荐类别成功");
        }
        return R.error().message("新增推荐类别失败");
    }
    //6、获取推荐类型分页数据列表
    @ApiOperation(value = "获取推荐类型分页数据列表")
    @GetMapping(value = "/get-ad-type-page/{pageNum}/{pageSize}")
    public R getAdTypePage(
            @ApiParam(value = "当前页") @PathVariable(value = "pageNum") Long pageNum,
            @ApiParam(value = "每页包含的条数") @PathVariable(value = "pageSize") Long pageSize){
        Page<AdType> page =  adTypeService.getAdTypePage(pageNum,pageSize);
        return R.ok().message("获取推荐类型分页数据列表成功").data("page",page);
    }

}

