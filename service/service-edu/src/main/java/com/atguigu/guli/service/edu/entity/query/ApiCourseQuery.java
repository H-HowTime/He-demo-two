package com.atguigu.guli.service.edu.entity.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hehao
 * @create 2020-12-29 8:40
 */
@Data
@ApiModel(value = "课程查询对象(用户)")
public class ApiCourseQuery {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "二级课程分类ID")
    private String subjectId;

    @ApiModelProperty(value = "一级课程分类ID")
    private String subjectParentId;

    @ApiModelProperty(value = "课程销售价格排序")
    private String priceSort;

    @ApiModelProperty(value = "销售数量排序")
    private String buyCountSort;

    @ApiModelProperty(value = "浏览数量")
    private String viewCount;

    @ApiModelProperty(value = "课程发布时间排序")
    private String publishTimeSort;

    @ApiModelProperty(value = "价格排序方式(1为正序,2为倒序")
    private Integer priceSortType;
}
