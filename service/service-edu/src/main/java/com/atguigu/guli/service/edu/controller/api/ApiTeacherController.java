package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherViewObject;
import com.atguigu.guli.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
//@CrossOrigin  由gateway网关全局路由配置 //解决跨域问题
@Api(tags = "讲师管理模块(用户)")
@RestController
@RequestMapping("/api/edu/teacher")
public class ApiTeacherController {

    @Autowired
    private TeacherService teacherService;

    //查询所有讲师
    @ApiOperation(value = "查询所有讲师")
    @GetMapping(value = "/list")
    public R selectList() {
        List<Teacher> list = teacherService.list(null);
        return R.ok().data("items", list).message("查询数据成功");
    }

    //查询指定讲师
    @ApiOperation(value = "查询指定讲师")
    @GetMapping(value = "/get/{id}")
    public R getTeacherById(
            @ApiParam(value = "讲师id",required = true) @PathVariable String id) {
        Teacher teacher = teacherService.getById(id);
        return R.ok().data("teacher", teacher);
    }
}

