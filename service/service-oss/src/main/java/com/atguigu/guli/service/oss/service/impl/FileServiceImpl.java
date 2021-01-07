package com.atguigu.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.utils.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author hehao
 * @create 2020-12-20 16:33
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private OssProperties ossProperties;

    @Value("${aliyun.oss.scheme}")
    String scheme;
    String endpoint;
    String accessKeyId;
    String accessKeySecret;
    String bucketName;

    @PostConstruct //jdk提供的初始化方法，当Javabean的构造器被调用后立即执行
    private void init() {
//        scheme = ossProperties.getScheme();
        endpoint = ossProperties.getEndpoint();
        accessKeyId = ossProperties.getAccessKeyId();
        accessKeySecret = ossProperties.getAccessKeySecret();
        bucketName = ossProperties.getBucketName();
    }

    @Override
    public String uploadFile(MultipartFile multipartFile, String module) {
        log.info("nacos配置中心阿里云图片：{}", ossProperties);
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(scheme + endpoint,
                accessKeyId, accessKeySecret);
        try {
            // 上传文件流。
            InputStream inputStream = multipartFile.getInputStream();
            // 获取文件名
            String filename = multipartFile.getOriginalFilename();
            //获取文件名的后缀
            String suffix = filename.substring(filename.lastIndexOf("."));
            // 将文件名和模块名进行拼接，并实现按照日期yyyy/MM/dd来分组
            String date = new DateTime().toString("/yyyy/MM/dd/");
            //再根据UUID生成一个唯一的字符串
            String uuid = UUID.randomUUID().toString().replace("-", "");

            //进行文件名拼接
            filename = module + date + uuid + suffix;

            ossClient.putObject(bucketName, filename, inputStream);

            //返回文件路径 https://hehao-file.oss-cn-shanghai.aliyuncs.com/1.JPG
            String filePath = scheme + bucketName + "." + endpoint + "/" + filename;
            System.out.println(filePath);

            // 关闭OSSClient。
            ossClient.shutdown();

            return filePath;
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public void deleteFile(String filePath, String module) {
        // http://hehao-file.oss-cn-shanghai.aliyuncs.com/teacher/2020/12/20/9b75107d2d9849298e5636726d2445ba.JPG

        String objectName = filePath.substring(filePath.lastIndexOf(module));

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(scheme + endpoint, accessKeyId, accessKeySecret);

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public void batchDelFile(List<String> paths, String module) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(scheme + endpoint, accessKeyId, accessKeySecret);
        // 删除文件。key等同于ObjectName，表示删除OSS文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        List<String> keys = new ArrayList<String>();
        for (String path : paths) {
            String key = path.substring(path.lastIndexOf(module));
            keys.add(key);
        }
        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
