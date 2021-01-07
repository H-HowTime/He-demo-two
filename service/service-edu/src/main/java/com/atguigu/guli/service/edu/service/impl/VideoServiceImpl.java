package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.feign.VodFeignClient;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Transactional
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VodFeignClient vodFeignClient;

    @Override
    public R deleteVideo(String videoId) {
        //先删除视频
        Video video = baseMapper.selectById(videoId);
        if (video == null) {
            return R.error().message("删除的课时不存在");
        }
        String videoSourceId = video.getVideoSourceId();
        R r = vodFeignClient.deleteVideo(videoSourceId);
        baseMapper.deleteById(videoId);
        return r;
    }
}
