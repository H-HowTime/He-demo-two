package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.edu.feign.OssFeignClient;
import com.atguigu.guli.service.edu.feign.VodFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hehao
 * @create 2020-12-22 17:31
 */
@Service
@Slf4j //开启日志
public class VodFeignClientFallback implements VodFeignClient {

    @Override
    public R deleteVideo(String videoId) {
        log.error("远程调用删除视频文件失败");
        return R.setResult(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
    }

    @Override
    public R deleteVideoList(List<String> videoIds) {
        log.error("远程调用批量删除视频文件失败");
        return R.setResult(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
    }
}
