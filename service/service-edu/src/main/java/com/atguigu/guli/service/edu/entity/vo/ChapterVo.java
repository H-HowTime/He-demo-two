package com.atguigu.guli.service.edu.entity.vo;

import com.atguigu.guli.service.edu.entity.Video;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hehao
 * @create 2020-12-25 19:15
 */
@Data
@ApiModel(value = "chapterVo对象", description = "回传给前端的chapter数据封装")
public class ChapterVo {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "课程ID")
    private String courseId;

    @ApiModelProperty(value = "章节名称")
    private String title;

    @ApiModelProperty(value = "显示排序")
    private Integer sort;

    @ApiModelProperty(value = "课时video列表")
    public List<Video> children = new ArrayList<Video>();
}
