package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.entity.vo.TeacherViewObject;
import com.atguigu.guli.service.edu.feign.OssFeignClient;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.xml.internal.bind.v2.TODO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
//@CrossOrigin  由gateway网关全局路由配置 //解决跨域问题，springMVC4.2以上版本支持
@Slf4j //开启统一日志处理 spring boot 使用logback作为日志实现的框架。采用slf4j api
@Api(tags = "讲师管理模块") //标注该应用在swagger2页面中的显示
@RestController
@RequestMapping("/admin/edu/teacher")
public class AdminTeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private OssFeignClient ossFeignClient;

    //查询所有讲师
    @ApiOperation(value = "查询所有讲师")
    @GetMapping(value = "/list")
    public R selectList() {
        List<Teacher> list = teacherService.list(null);
        System.out.println(666);
        return R.ok().data("items", list).message("查询数据成功");
    }

    //分页查询所有讲师数据
//    @ApiOperation(value = "分页查询讲师数据")
//    @GetMapping(value = "/page/{pageNum}/{pageSize}")
//    public R pageT(
//            @ApiParam(value = "页码") @PathVariable Integer pageNum,
//            @ApiParam(value = "每页显示的条数")@PathVariable Integer pageSize){
//        Page<Teacher> page = teacherService.page(new Page<Teacher>(pageNum, pageSize));
//        return R.ok().data("page",page);
//    }
    //带有条件的分页查询
    @ApiOperation(value = "分页查询讲师数据(带有条件)")
    @GetMapping(value = "/page/{pageNum}/{pageSize}")
    public R pageT(
            @ApiParam(value = "页码") @PathVariable Integer pageNum,
            @ApiParam(value = "每页显示的条数") @PathVariable Integer pageSize,
            @ApiParam(value = "讲师查询条件") TeacherQuery teacherQuery) {
        //带有条件的查询，业务逻辑应该在server层实现
        Page<Teacher> page = teacherService.pageByTeacherQuery(pageNum, pageSize, teacherQuery);
        return R.ok().data("page", page);
    }

    /**
     * 请求参数： get方式 在请求路径后拼接?k=v&k1=v1 使用@ResquestParam("k")接收 或者使用pojo对象接收（k=v&k1=v1 k的值要和pojo属性名一致）
     * 请求体： post方式在请求体中以参数的形式提交（和get方式一样） / 请求体中以json字符串的形式提交 使用@ResquestBody接受
     * 路径参数： /remove/1  @DeleteMapping(/remove/{id}) @PathVariable("id")来接收
     * <p>
     * 如果请求参数较少，使用路径参数、
     * 如果参数较多 get方式提交 一般使用请求参数pojo方式入参
     * 如果其他方式参数较多，一般采用请求体json字符串方式入参
     * <p>
     * 常见的响应报文响应状态码
     * 200 成功
     * 302 重定向
     * 400 请求参数和后台要求不一致（请求报文错误）
     * 403 没有权限
     * 404 路径访问的资源不存在
     * 405 请求方式不被支持
     * 5xx 服务器内容错误
     * 503 一般访问网关时，路径没有对应的路由匹配
     */
    //逻辑删除
    @ApiOperation(value = "删除一名讲师")
    @DeleteMapping(value = "/remove/{id}")
    public R removeT(@ApiParam(value = "讲师ID", required = true) @PathVariable(value = "id") String id) { //主键策略使用雪花算法，19位数字
        //获取讲师头像地址
        Teacher teacher = teacherService.getById(id);
        String avatarPath = teacher.getAvatar();
        boolean b = teacherService.removeById(id);
        if (b) {
            //删除讲师的同时删除阿里云中的讲师头像 需要通过远程访问service-oss应用删除接口
            //TODO 问题：删除讲师时为逻辑删除，而讲师头像是在阿里云中直接删除，怎么解决？
            R r = ossFeignClient.deleteFile(avatarPath, "teacher");
            return r;
        }
        return R.error().message("删除失败");
    }

    //添加一名讲师
    @ApiOperation(value = "增加一名讲师")
    @PostMapping(value = "/add")
    public R addTeacher(@RequestBody Teacher teacher) {
        //设置默认值
        teacher.setJoinDate(new Date()); //加入日期
        if (StringUtils.isEmpty(teacher.getAvatar())) {
            teacher.setAvatar("https://hehao-file.oss-cn-shanghai.aliyuncs.com/1bb0bf39b8daf3832e1a20473518bc65.jpg");
        }
        boolean b = teacherService.save(teacher);
        if (b) {
            return R.ok().message("添加成功");
        }
        return R.error().message("讲师姓名已存在，添加失败");
    }

    //更新一名讲师
    //查询指定讲师
    @ApiOperation(value = "查询指定讲师")
    @GetMapping(value = "/get/{id}")
    public R getTById(@PathVariable String id) {
        TeacherViewObject viewObject = teacherService.getVOById(id);
        return R.ok().data("vo", viewObject);
    }

    //更新讲师
    @ApiOperation(value = "更新指定讲师")
    @PostMapping(value = "/update")
    public R updateById(@RequestBody Teacher teacher) {
        boolean b = teacherService.updateById(teacher);
        if (b) {
            return R.ok().message("更新成功");
        }
        return R.error().message("没有对应数据，更新失败");
    }

    //批量删除讲师
    @ApiOperation(value = "批量删除讲师")
    @DeleteMapping(value = "/batchDel")
    public R batchDel(@RequestParam List<String> ids, @RequestBody List<String> paths) {
        //批量删除讲师
        //参数的提交方式1：url？id=1&id=2...  接接收时需要使用原生的request对象来获取参数
        //参数的提交方式2：url？id=1,2,3 后端springmvc可以直接使用list集合来接收id的多个参数
        System.out.println(ids);
        System.out.println(paths);
        boolean b = teacherService.removeByIds(ids);
        if (b) {
            //删除讲师的同时删除阿里云中的讲师头像 需要通过远程访问service-oss应用删除接口
            R r = ossFeignClient.batchDelFile(paths, "teacher");
            return r;
        }
        return R.error().message("批量删除讲师失败");
    }

    //获取讲师姓名
    @ApiOperation(value = "获取讲师姓名列表")
    @GetMapping(value = "/name")
    public R getTeacherName() {
        List<Map<String, String>> names = teacherService.getTeacherName();
        return R.ok().message("查询讲师姓名列表成功").data("names", names);
    }

    //远程服务调用测试
    @ApiOperation(value = "openfeign远程掉用测试")
    @GetMapping(value = "/openfeign")
    public R openfeignTest(String str) {
        R r = ossFeignClient.test(str);
        return r;
    }
}

