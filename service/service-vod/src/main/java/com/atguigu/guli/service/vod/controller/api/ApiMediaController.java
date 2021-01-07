package com.atguigu.guli.service.vod.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.vod.service.MediaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author hehao
 * @create 2020-12-27 18:04
 */
@RefreshScope
@Slf4j //开启统一日志管理
//@CrossOrigin  由gateway网关全局路由配置 //解决跨域问题
@Api(tags = "视频管理模块(用户)")
@RestController
@RequestMapping(value = "/api/vod/media")
public class ApiMediaController {

    @Autowired
    private MediaService mediaService;

    //1、课时视频上传
    @ApiOperation(value = "课时视频上传")
    @PostMapping(value = "/upload-video")
    public R uploadVideo(
            @ApiParam(value = "视频文件", required = true)
            @RequestParam(value = "file") MultipartFile file) {
        String videoId = mediaService.updateVideo(file);
        return R.ok().message("文件上传阿里云成功").data("videoId", videoId);
    }

    //2、获取播放地址（未加密视频）
    @ApiOperation(value = "获取播放地址")
    @GetMapping(value = "/auth/get-play-url/{videoId}")
    public R getPlayUrl(
            @ApiParam(value = "视频id", required = true) @PathVariable(value = "videoId") String videoId) {
        String playUrl = mediaService.getPlayUrl(videoId);
        System.out.println(playUrl);
        return R.ok().message("获取播放地址成功").data("playUrl", playUrl);
    }

    //3、获取播放凭证（加密视频）
    @ApiOperation(value = "获取播放凭证")
    @GetMapping(value = "/auth/get-play-auth/{videoId}")
    public R getPlayAuth(
            @ApiParam(value = "视频id", required = true) @PathVariable(value = "videoId") String videoId) {
        String playAuth = mediaService.getPlayAuth(videoId);
        return R.ok().message("获取播放凭证成功").data("playAuth", playAuth);
    }
    //4、删除课时视频
    @ApiOperation(value = "删除课时视频")
    @DeleteMapping(value = "/delete-video/{videoId}")
    public R deleteVideo(
            @ApiParam(value = "视频id", required = true) @PathVariable(value = "videoId") String videoId) {
        mediaService.deleteVideo(videoId);
        return R.ok().message("删除视频成功");
    }
    //6、批量删除视频
    @ApiOperation(value = "批量删除视频")
    @DeleteMapping(value = "/delete-video-list")
    public R deleteVideoList(
            @ApiParam(value = "视频id集合", required = true) @RequestBody List<String> videoIds) {
        mediaService.deleteVideoList(videoIds);
        return R.ok().message("批量删除视频成功");
    }
}
