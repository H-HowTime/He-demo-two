package com.atguigu.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.*;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.vod.service.MediaService;
import com.atguigu.guli.service.vod.utils.VodProperties;
import com.atguigu.guli.service.vod.utils.VodUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author hehao
 * @create 2020-12-27 18:27
 */
@Slf4j
@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    private VodProperties vodProperties;

    private String accessKeyId; //阿里云KeyId
    private String accessKeySecret; //阿里云KeySecret
    private String templateGroupId; //模板组ID（可选）
    private String workflowId; //工作流ID(可选)

    @PostConstruct //jdk提供的初始化方法，当Javabean的构造器被调用后立即执行
    public void init() {
        accessKeyId = vodProperties.getAccessKeyId();
        accessKeySecret = vodProperties.getAccessKeySecret();
        templateGroupId = vodProperties.getTemplateGroupId();
        workflowId = vodProperties.getWorkflowId();
    }

    @Override
    public String updateVideo(MultipartFile file) {
        log.info("nacos配置中心阿里云视频：{}", vodProperties);
        try {
            //获取视频文件的输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名
            String fileName = file.getOriginalFilename();
            //设置视频标题
            String title = fileName;

            UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
            /* 视频描述(可选) */
            //request.setDescription("视频描述");
            /* 封面图片(可选) */
            request.setCoverURL("https://hehao-file.oss-cn-shanghai.aliyuncs.com/1bb0bf39b8daf3832e1a20473518bc65.jpg");
            /* 模板组ID(可选) */
//        request.setTemplateGroupId("c38a90a68e76afc2051dcdc49e673c93");
            /* 工作流ID(可选) */
            request.setWorkflowId(workflowId);

            UploadVideoImpl uploader = new UploadVideoImpl();

            UploadStreamResponse response = uploader.uploadStream(request);

            System.out.println("RequestId=" + response.getRequestId());  //请求视频点播服务的请求ID
            if (response.isSuccess()) {
                System.out.println("VideoId=" + response.getVideoId());
                //响应成功 返回VideoId
                return response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                log.error("视频上传到阿里云失败：< VideoId=" + response.getVideoId() +
                        "ErrorCode=" + response.getCode() +
                        "ErrorMessage=" + response.getMessage() + " >");
                throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
            }
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }

    @Override
    public String getPlayUrl(String videoId) {
        try {
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            request.setVideoId(videoId); //视频id
            GetPlayInfoResponse response = VodUtil.initVodClient(accessKeyId, accessKeySecret).getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            String playURL = playInfoList.get(0).getPlayURL();
            System.out.println("playURL = " + playURL);
            //Base信息
            System.out.println("VideoBase.Title = " + response.getVideoBase().getTitle());
            //返回播放地址
            return playURL;
        } catch (Exception e) {
            log.error("获取播放凭证失败:< ErrorMessage =" + e.getLocalizedMessage() + " >");
            throw new GuliException(ResultCodeEnum.FETCH_VIDEO_UPLOADAUTH_ERROR);
        }
    }

    @Override
    public String getPlayAuth(String videoId) {
        try {
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId); //视频id
            request.setAuthInfoTimeout(3000l); //播放凭证过期时间 以秒为单位
            GetVideoPlayAuthResponse response = VodUtil.initVodClient(accessKeyId, accessKeySecret).getAcsResponse(request);
            //播放凭证 是一个加密字符串，只支持阿里云播放器播放
            System.out.print("PlayAuth = " + response.getPlayAuth());
            //VideoMeta信息
            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle());
            //返回播放凭证
            return response.getPlayAuth();
        } catch (Exception e) {
            log.error("获取播放凭证失败:< ErrorMessage =" + e.getLocalizedMessage() + " >");
            throw new GuliException(ResultCodeEnum.FETCH_PLAYAUTH_ERROR);
        }
    }

    @Override
    public void deleteVideo(String videoId) {
        try {
            DeleteVideoRequest request = new DeleteVideoRequest();
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(videoId);
            DeleteVideoResponse response =
                    VodUtil.initVodClient(accessKeyId, accessKeySecret).getAcsResponse(request);
            System.out.println("RequestId = " + response.getRequestId());
        } catch (Exception e) {
            log.error("删除视频失败:< ErrorMessage =" + e.getLocalizedMessage() + " >");
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }

    @Override
    public void deleteVideoList(List<String> videoIds) {
        //排除空值
        videoIds.removeAll(Collections.singleton(null));
        //将集合转化为以逗号分隔的字符串
        String vIds = videoIds.toString().replaceAll("^\\[| |\\]$", "");
        try {
            DeleteVideoRequest request = new DeleteVideoRequest();
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(vIds);
            DeleteVideoResponse response =
                    VodUtil.initVodClient(accessKeyId, accessKeySecret).getAcsResponse(request);
            System.out.println("RequestId = " + response.getRequestId());
        } catch (Exception e) {
            log.error("批量删除视频失败:< ErrorMessage =" + e.getLocalizedMessage() + " >");
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }
}

