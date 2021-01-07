package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
public interface VideoService extends IService<Video> {

    R deleteVideo(String videoId);
}
