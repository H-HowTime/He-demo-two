package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.edu.feign.OssFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hehao
 * @create 2020-12-22 17:31
 */
@Service
@Slf4j //开启日志
public class OssFeignClientFallback implements OssFeignClient {
    @Override
    public R deleteFile(String filePath, String module) {
        log.error("远程调用删除文件失败");
        return R.setResult(ResultCodeEnum.REMOTE_REQUEST_ERROR);
    }

    @Override
    public R test(String str) {
        log.info("返回sentinel熔断降级保护托底数据");
        System.out.println("返回sentinel熔断降级保护托底数据");
        return R.error().message("托底数据");
    }

    @Override
    public R batchDelFile(List<String> paths, String module) {
        log.error("远程调用批量删除文件失败");
        return R.setResult(ResultCodeEnum.REMOTE_REQUEST_ERROR);
    }
}
