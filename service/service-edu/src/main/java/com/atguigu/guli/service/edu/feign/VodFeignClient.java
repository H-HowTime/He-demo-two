package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.fallback.VodFeignClientFallback;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author hehao
 * @create 2020-12-27 23:57
 */
@Service //将该接口放进容器中
@FeignClient(value = "service-vod", fallback = VodFeignClientFallback.class) //标识要调用哪个微服务,并指定当出现异常时返回兜底数据
public interface VodFeignClient {

    //删除课时视频
    @DeleteMapping(value = "/admin/vod/media/delete-video/{videoId}")
    public R deleteVideo(@PathVariable(value = "videoId") String videoId);

    //批量删除视频
    @DeleteMapping(value = "/admin/vod/media/delete-video-list")
    public R deleteVideoList(@RequestBody List<String> videoIds);
}
