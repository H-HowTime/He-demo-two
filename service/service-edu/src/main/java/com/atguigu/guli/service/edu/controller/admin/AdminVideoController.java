package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
//@CrossOrigin  由gateway网关全局路由配置 //解决跨域问题
@Api(tags = "课时管理模块")
@RestController
@RequestMapping("/admin/edu/video")
public class AdminVideoController {

    @Autowired
    private VideoService videoService;

    //1、新增课时
    @ApiOperation(value = "新增课时")
    @PostMapping(value = "/save-Video")
    public R saveVideo(
            @ApiParam(value = "课时信息", required = true) @RequestBody Video video) {
        boolean b = videoService.save(video);
        if (b) {
            return R.ok().message("新增课时成功");
        }
        return R.error().message("新增课时失败");
    }

    //2、根据id删除课时
    @ApiOperation(value = "根据id删除课时")
    @DeleteMapping(value = "/delete-video/{videoId}")
    public R deleteVideo(
            @ApiParam(value = "课时ID", required = true) @PathVariable(value = "videoId") String videoId) {
        return videoService.deleteVideo(videoId);

    }

    //3、根据id获取指定课时
    @ApiOperation(value = "根据id获取指定课时")
    @GetMapping(value = "/get-Video/{videoId}")
    public R getVideo(
            @ApiParam(value = "课时ID", required = true) @PathVariable(value = "videoId") String videoId) {
        Video video = videoService.getById(videoId);
        if (video != null) {
            return R.ok().message("获取指定课时成功").data("video",video);
        }
        return R.error().message("获取指定课时失败");
    }

    //4、更新课时
    @ApiOperation(value = "更新课时")
    @PutMapping(value = "/update-Video")
    public R updateVideo(
            @ApiParam(value = "课时信息", required = true) @RequestBody Video video) {
        boolean b = videoService.updateById(video);
        if (b) {
            return R.ok().message("更新课时成功");
        }
        return R.error().message("更新课时失败");
    }
}

