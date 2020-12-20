package com.atguigu.guli.service.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author hehao
 * @create 2020-12-20 16:32
 */
public interface FileService {
    String uploadFile(MultipartFile multipartFile, String module);

    void deleteFile(String filePath);
}
