package com.atguigu.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hehao
 * @create 2021-01-07 15:20
 */
@Data
@ApiModel(value = "CollectCourseVo对象", description = "回传给前端的收藏数据封装")
public class CollectCourseVo {

    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "课程ID")
    private String courseId;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "价格")
    private String price;
    @ApiModelProperty(value = "课时数")
    private Integer lessonNum;
    @ApiModelProperty(value = "封面")
    private String cover;
    @ApiModelProperty(value = "收藏时间")
    private String gmtCreate;
    @ApiModelProperty(value = "讲师")
    private String teacherName;
}
