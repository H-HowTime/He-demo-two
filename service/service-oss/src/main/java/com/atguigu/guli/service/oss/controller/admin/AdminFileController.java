package com.atguigu.guli.service.oss.controller.admin;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hehao
 * @create 2020-12-20 15:54
 */
@Api(tags = "文件管理模块") //标注该应用在swagger2页面中的显示
@CrossOrigin //解决跨域问题
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
    public R deleteFile(String filePath) {
        fileService.deleteFile(filePath);
        return R.ok().message("文件删除成功");
    }
}
