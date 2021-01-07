package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseViewObject;
import com.atguigu.guli.service.edu.feign.OssFeignClient;
import com.atguigu.guli.service.edu.feign.VodFeignClient;
import com.atguigu.guli.service.edu.service.CourseService;
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
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
//@CrossOrigin  由gateway网关全局路由配置 //解决跨域问题
@Api(tags = "课程管理模块")
@RestController
@RequestMapping("/admin/edu/course")
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private VodFeignClient vodFeignClient;

    @Autowired
    private OssFeignClient ossFeignClient;

    //1、将课程基本信息保存到数据库
    @ApiOperation(value = "保存课程基本信息")
    @PostMapping(value = "/saveCourseInfo")
    public R saveCourseInfo(@ApiParam(value = "表单数据", required = true) @RequestBody CourseInfoForm courseInfoForm) {
        String courseId = courseService.saveCourseInfo(courseInfoForm);
        return R.ok().data("courseId", courseId);
    }

    //2、根据id查询课程列表
    @ApiOperation(value = "通过id获取课程基本信息")
    @GetMapping(value = "/course-info/{id}")
    public R getCourseInfoById(@ApiParam(value = "课程id")@PathVariable String id) {
        CourseInfoForm courseInfoForm = courseService.getCourseInfoById(id);
        if(courseInfoForm == null){
            return R.ok().message("查询的课程不存在");
        }
        return R.ok().data("courseInfoForm",courseInfoForm);
    }
    //3、获取课程基础信息
    @ApiOperation(value = "获取课程基础信息")
    @GetMapping(value = "/course-info-list")
    public R courseInfoList() {
        List<CourseViewObject> courseInfoList = courseService.courseInfoList();
        return R.ok().data("courseInfoList", courseInfoList).message("查询课程基础数据成功");
    }

    //4、获取课程基础分页信息
    @ApiOperation(value = "获取课程基础分页信息")
    @GetMapping(value = "/course-info-page-list/{pageNum}/{pageSize}")
    public R courseInfoPageList(
            @ApiParam(value = "页码") @PathVariable Long pageNum,
            @ApiParam(value = "每页显示的条数") @PathVariable Long pageSize,
            @ApiParam(value = "课程查询条件") CourseQuery courseQuery) {
        //带有条件的查询，业务逻辑应该在server层实现
        IPage<CourseViewObject> page = courseService.courseInfoPageList(pageNum,pageSize,courseQuery);
        return R.ok().data("page",page);
    }
    //5、更新课程基础信息
    @ApiOperation(value = "更新课程基础信息")
    @PutMapping(value = "update-course-info/{courseId}")
    public R updateCourseIfo(
            @ApiParam(value = "课程基础信息") @RequestBody CourseInfoForm courseInfoForm,
            @ApiParam(value = "课程ID") @PathVariable String courseId){
        courseService.updateCourseIfo(courseInfoForm,courseId);
        return R.ok().message("更新课程基础数据成功");

    }
    //6、根据课程id获取课程发布预览信息
    @ApiOperation(value = "根据课程id获取课程发布预览信息")
    @GetMapping(value = "/course-publish/{courseId}")
    public R coursePublish(
            @ApiParam(value = "课程id")@PathVariable(value = "courseId") String courseId){
        CoursePublishVo coursePublishVo = courseService.coursePublish(courseId);
        if(coursePublishVo == null){
            return R.error().message("查询的课程信息不存在");
        }
        return R.ok().data("coursePublishVo",coursePublishVo);
    }
    //7、根据课程id更新课程发布状态
    @ApiOperation(value = "根据课程id更新课程发布状态")
    @PutMapping(value = "/change-course-status/{courseId}")
    public R changeCourseStatusById(
            @ApiParam(value = "课程id")@PathVariable(value = "courseId") String courseId){
        boolean b = courseService.changeCourseStatusById(courseId);
        if(b){
            return R.ok().message("更新课程状态成功");
        }
        return R.error().message("更新课程状态失败");
    }
    //8、根据课程id删除课程
    @ApiOperation(value = "根据课程id删除课程")
    @DeleteMapping(value = "/delete-course/{courseId}")
    public R deleteCourseById(
            @ApiParam(value = "课程id")@PathVariable(value = "courseId") String courseId){
        //删除封面：OSS 通过课程id查询 删除课程封面
        String filePath = courseService.removeCoverById(courseId);
        ossFeignClient.deleteFile(filePath, "cover");
        //通过课程id删除视频--课程id查询视频资源video_source_id 远程调用vod服务批量删除视频
        List<String> Ids =  courseService.getVideoIds(courseId);
        System.out.println(Ids);
        vodFeignClient.deleteVideoList(Ids);
        //删除课程
        boolean b = courseService.deleteCourseById(courseId);
        if(b){
            return R.ok().message("删除课程成功");
        }
        return R.error().message("删除课程失败");
    }

}

