package com.atguigu.guli.service.vod.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hehao
 * @create 2020-12-27 18=19
 */
@Component
@Data
@ConfigurationProperties(prefix = "aliyun.vod")
public class VodProperties {

    private String accessKeyId; //阿里云KeyId
    private String accessKeySecret; //阿里云KeySecret
    private String templateGroupId; //模板组ID（可选）
    private String workflowId; //工作流ID(可选)
}
