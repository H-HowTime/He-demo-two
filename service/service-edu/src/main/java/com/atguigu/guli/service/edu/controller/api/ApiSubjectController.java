package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.vo.SubjectViewObject;
import com.atguigu.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
//@CrossOrigin  由gateway网关全局路由配置 //解决跨域问题
@Api(tags = "课程分类管理模块(用户)")
@RestController
@RequestMapping("/api/edu/subject")
public class ApiSubjectController {

    @Autowired
    private SubjectService subjectService;

    //查询课程分类嵌套列表
    @ApiOperation(value = "查询课程分类嵌套列表")
    @GetMapping(value = "/nested-list")
    public R SubjectNestedList() {
        List<SubjectViewObject> nestedList = subjectService.SubjectNestedList();
        System.out.println(nestedList);
        return R.ok().message("查询课程分类嵌套列表成功").data("nestedList", nestedList);
    }
}

