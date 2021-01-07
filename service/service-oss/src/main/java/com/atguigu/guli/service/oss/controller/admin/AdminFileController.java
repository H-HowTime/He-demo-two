package com.atguigu.guli.service.oss.controller.admin;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.SchemaOutputResolver;
import java.util.List;

/**
 * @author hehao
 * @create 2020-12-20 15:54
 */
@RefreshScope
@Api(tags = "文件管理模块") //标注该应用在swagger2页面中的显示
//@CrossOrigin  由gateway网关全局路由配置 //解决跨域问题
@Slf4j //开启统一日志处理 spring boot 使用logback作为日志实现的框架。采用slf4j api
@RestController
@RequestMapping(value = "/admin/oss/file")
public class AdminFileController {

    @Autowired
    private FileService fileService;

    //上传文件
    @ApiOperation(value = "阿里云文件上传")
    @PostMapping(value = "/upload")
    //参数一：上传的文件对象 参数二：文件保存的模块
    public R uploadFile(@RequestParam(value = "file") MultipartFile multipartFile, @RequestParam(value = "module") String module) {
        try {
            String filePath = fileService.uploadFile(multipartFile, module);
            return R.ok().data("path", filePath);
        } catch (Exception e) {
            return R.setResult(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    //根据上传文件的地址删除文件
    @ApiOperation(value = "阿里云文件删除")
    @DeleteMapping(value = "/deleteFile")
    public R deleteFile(@RequestParam("filePath") String filePath,@RequestParam("module") String module) {
        fileService.deleteFile(filePath,module);
        return R.ok().message("文件删除成功");
    }
    //当批量删除讲师列表时 批量删除阿里云中的讲师头像
    //批量删除讲师头像
    @ApiOperation(value = "批量删除阿里云中的讲师头像")
    @DeleteMapping(value = "/batchDelFile")
    public R batchDelFile(@RequestBody List<String> paths,@RequestParam("module") String module){
        fileService.batchDelFile(paths,module);
        return R.ok();
    }

    //远程调用测试
    @Value(value = "${server.port}")
    private String port;

    @ApiOperation(value = "远程调用测试")
    @GetMapping(value = "/test")
    public R test(@RequestParam(value = "str") String str){
        System.out.println(str);
        System.out.println(port);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return R.ok();
    }

}
