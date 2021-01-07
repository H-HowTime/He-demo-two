package com.atguigu.guli.service.edu.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hehao
 * @create 2020-12-23 10:12
 */
@Data
@ApiModel(value = "SubjectViewObject对象", description = "回传给前端的subject数据封装")
public class SubjectViewObject {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "课程类别名称")
    private String title;

    @ApiModelProperty(value = "课程二级分类列表")
    public List<SubjectViewObject> children = new ArrayList<>();
}
