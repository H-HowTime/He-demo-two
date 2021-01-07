package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.ApiCourseQuery;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CourseDetailApiVo;
import com.atguigu.guli.service.edu.entity.vo.CourseViewObject;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
//@CrossOrigin  由gateway网关全局路由配置 //解决跨域问题
@Api(tags = "课程管理模块(用户)")
@RestController
@RequestMapping("/api/edu/course")
public class ApiCourseController {

    @Autowired
    private CourseService courseService;

    //1、根据讲师id查询课程
    @ApiOperation(value = "根据讲师id查询课程")
    @GetMapping(value = "/get/{Tid}")
    public R getCourseByTid(
            @ApiParam(value = "讲师id", required = true) @PathVariable String Tid) {
        List<Course> courses = (List<Course>) courseService.getCourseByTid(Tid);
        return R.ok().data("courses", courses);
    }

    //2、获取课程信息
    @ApiOperation(value = "获取课程信息")
    @GetMapping(value = "/course-list")
    public R courseList(
            @ApiParam(value = "课程查询条件") ApiCourseQuery apiCourseQuery) {
        //带有条件的查询，业务逻辑应该在server层实现
        List<Course> courseList = courseService.courseList(apiCourseQuery);
        return R.ok().message("查询课程信息成功").data("courseList", courseList);
    }

    //3、获取课程详情信息
    @ApiOperation(value = "根据课程id获取课程详情信息")
    @GetMapping(value = "/course-detail/{courseId}")
    public R courseDetailById(
            @ApiParam(value = "课程id") @PathVariable(value = "courseId") String courseId) {
        CourseDetailApiVo courseDetailApiVo = courseService.courseDetailById(courseId);
        return R.ok().message("查询课程详情信息成功").data("courseDetailApiVo", courseDetailApiVo);
    }

    //4、订单服务远程调用获取课程信息
    @ApiOperation(value = "订单服务远程调用获取课程信息")
    @GetMapping(value = "/course-order/{courseId}")
    public R courseOrderById(
            @ApiParam(value = "课程id") @PathVariable(value = "courseId") String courseId) {
        CourseDto courseDto = courseService.courseOrderById(courseId);
        return R.ok().message("订单服务远程调用获取课程信息成功").data("courseDto", courseDto);
    }

    //5、订单服务远程调用更改商品销量
    @ApiOperation(value = "订单服务远程调用更改商品销量")
    @PutMapping(value = "update-course-buy-count/{courseId}")
    public R updateCourse2BuyCount(
            @ApiParam(value = "课程ID") @PathVariable String courseId) {
        boolean isSuccess = courseService.updateCourse2BuyCount(courseId);
        if (isSuccess) {
            return R.ok().message("订单服务远程调用更改商品销量成功");
        }
        return R.error().message("订单服务远程调用更改商品销量失败");
    }
}

