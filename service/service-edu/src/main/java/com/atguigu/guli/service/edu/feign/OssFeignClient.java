package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.fallback.OssFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author hehao
 * @create 2020-12-21 17:54
 */
@Service //将该接口放进容器中
@FeignClient(value = "service-oss",fallback = OssFeignClientFallback.class) //标识要调用哪个微服务,并指定当出现异常时返回兜底数据
public interface OssFeignClient {

    @DeleteMapping(value = "/admin/oss/file/deleteFile")
    public R deleteFile(@RequestParam("filePath") String filePath, @RequestParam("module") String module);

    @GetMapping(value = "/admin/oss/file/test")
    public R test(@RequestParam("str") String str);

    @DeleteMapping(value = "/admin/oss/file/batchDelFile")
    public R batchDelFile(@RequestBody List<String> paths, @RequestParam("module") String module);
}
