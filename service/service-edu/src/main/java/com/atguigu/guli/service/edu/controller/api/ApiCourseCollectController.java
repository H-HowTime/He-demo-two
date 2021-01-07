package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.edu.entity.vo.CollectCourseVo;
import com.atguigu.guli.service.edu.service.CourseCollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 课程收藏 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Api(tags = "收藏管理模块")
@RestController
@RequestMapping("/api/edu/course-collect")
public class ApiCourseCollectController {

    @Autowired
    private CourseCollectService courseCollectService;

    //1、判断是否收藏课程
    @ApiOperation(value = "判断用户是否收藏课程")
    @GetMapping(value = "/auth/is-collect-course/{courseId}")
    public R isCollectCourse(
            @ApiParam(value = "课程id", required = true) @PathVariable(value = "courseId") String courseId,
            HttpServletRequest request) {
        String memberId = JwtHelper.getId(request);
        boolean isCollect = courseCollectService.isCollectCourse(courseId, memberId);
        return R.ok().data("isCollect", isCollect);
    }

    //2、收藏课程
    @ApiOperation(value = "收藏课程")
    @PostMapping(value = "/auth/save-collect-course/{courseId}")
    public R saveCollectCourse(
            @ApiParam(value = "课程id", required = true) @PathVariable(value = "courseId") String courseId,
            HttpServletRequest request) {
        String memberId = JwtHelper.getId(request);
        courseCollectService.saveCollectCourse(courseId, memberId);
        return R.ok().message("收藏课程成功");
    }

    //3、获取收藏课程列表
    @ApiOperation(value = "获取收藏课程列表")
    @GetMapping(value = "/auth/get-collect-course-list")
    public R getCollectCourseList(HttpServletRequest request) {
        String memberId = JwtHelper.getId(request);
        List<CollectCourseVo> collectCourseVoList =  courseCollectService.getCollectCourseList(memberId);
        return R.ok().message("获取收藏课程列表成功").data("collectCourseVoList", collectCourseVoList);
    }

    //4、取消收藏
    @ApiOperation(value = "取消收藏")
    @DeleteMapping(value = "/auth/delete-collect-course/{courseId}")
    public R deleteCollectCourse(
            @ApiParam(value = "课程id", required = true) @PathVariable(value = "courseId") String courseId,
            HttpServletRequest request) {
        String memberId = JwtHelper.getId(request);
        courseCollectService.deleteCollectCourse(courseId, memberId);
        return R.ok().message("取消收藏成功");
    }
}

