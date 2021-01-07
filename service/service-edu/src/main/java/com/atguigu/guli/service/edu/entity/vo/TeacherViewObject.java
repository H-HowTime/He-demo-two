package com.atguigu.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author hehao
 * @create 2020-12-20 18:46
 */
@Data
@AllArgsConstructor
@ApiModel(value = "TeacherViewObject对象", description = "回传给前端的teacher数据封装")
public class TeacherViewObject {
    @ApiModelProperty(value = "讲师id")
    private String id;
    @ApiModelProperty(value = "讲师姓名")
    private String name;
    @ApiModelProperty(value = "讲师级别")
    private Integer level;
    @ApiModelProperty(value = "讲师排序")
    private Integer sort;
    @ApiModelProperty(value = "入驻时间")
    private Date joinDate;
    @ApiModelProperty(value = "讲师资历")
    private String career;
    @ApiModelProperty(value = "讲师简介")
    private String intro;
    @ApiModelProperty(value = "讲师头像")
    private String avatar;
}
