package com.atguigu.guli.service.oss.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hehao
 * @create 2020-12-20 16:14
 */
@Component
@Data //来时lombok 自动生成get、set方法
@ConfigurationProperties(prefix = "aliyun.oss") //
public class OssProperties {
    private String scheme;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
