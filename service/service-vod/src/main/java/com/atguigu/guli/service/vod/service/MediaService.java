package com.atguigu.guli.service.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author hehao
 * @create 2020-12-27 18:26
 */
public interface MediaService {
    String updateVideo(MultipartFile file);

    String getPlayUrl(String videoId);

    String getPlayAuth(String videoId);

    void deleteVideo(String videoId);

    void deleteVideoList(List<String> videoIds);
}
