package com.atguigu.guli.service.edu.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hehao
 * @create 2020-12-29 0:17
 */
@Data
@ApiModel(value = "CourseDetailApiVo对象", description = "回传给用户端的course详情数据封装")
public class CourseDetailApiVo {

    //课程部分(course表)
    @ApiModelProperty(value = "课程ID")
    private String id;

    @ApiModelProperty(value = "课程专业父级ID")
    private String subjectParentId;

    //课程分类部分(subject表)
    @ApiModelProperty(value = "一级分类")
    private String subjectParentTitle;

    @ApiModelProperty(value = "课程专业ID")
    private String subjectId;

    @ApiModelProperty(value = "二级分类")
    private String subjectTitle;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;

    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "总课时")
    private Integer lessonNum;

    @ApiModelProperty(value = "销售数量")
    private Long buyCount;

    @ApiModelProperty(value = "浏览数量")
    private Long viewCount;

    @ApiModelProperty(value = "课程状态 Draft未发布  Normal已发布")
    private String status;

    //课程简介部分(description表)
    @ApiModelProperty(value = "课程简介")
    private String description;

    //讲师部分(teacher表)
    @ApiModelProperty(value = "课程讲师ID")
    private String teacherId;

    @ApiModelProperty(value = "讲师姓名")
    private String name;

    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师")
    private Integer level;

    @ApiModelProperty(value = "讲师头像")
    private String avatar;

    //章节部分(chapter表) 使用ChapterVo接收
    @ApiModelProperty(value = "章节课时嵌套数据")
    private List<ChapterVo> chapters = new ArrayList<>();
}
