package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.vo.SubjectViewObject;
import com.atguigu.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Api(tags = "课程分类管理模块")
//@CrossOrigin  由gateway网关全局路由配置 //解决跨域问题
@RestController
@RequestMapping("/admin/edu/subject")
public class AdminSubjectController {

    @Autowired
    private SubjectService subjectService;

    //处理课程分类文件上传，保存课程分类到数据库中
    @ApiOperation(value = "课程分类文件上传")
    @PostMapping(value = "/import")
    public R importFile(MultipartFile file){
        //解析课程分类数据保存到数据库
        //1、引入easyExcel依赖
        //2、编写文件对应的JavaBean
        //3、编写监听器，每读一行回调一次
        subjectService.batchUploadSubject(file);
        return R.ok().message("课程分类数据上传成功");
    }

    //查询课程分类嵌套列表
    @ApiOperation(value = "查询课程分类嵌套列表")
    @GetMapping(value = "/nestedList")
    public R SubjectNestedList(){
        List<SubjectViewObject> nestedList = subjectService.SubjectNestedList();
        System.out.println(nestedList);
        return R.ok().message("查询课程分类嵌套列表成功").data("nestedList",nestedList);
    }

}

