package com.atguigu.guli.service.cms.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hehao
 * @create 2021-01-03 21:49
 */
@Data
@ApiModel(value = "AdVo对象", description = "回传给前端的ad数据封装")
public class AdVo {

    @ApiModelProperty(value = "广告ID")
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "所属类型")
    private String type;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
