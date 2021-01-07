package com.atguigu.guli.service.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hehao
 * @create 2021-01-05 12:55
 */
@Data
@ApiModel(value = "CourseDto对象", description = "订单服务远程调用获取课程信息数据封装")
public class MemberDto {

    @ApiModelProperty(value = "会员ID")
    private String id;

    @ApiModelProperty(value = "手机号")
    private String mobile;


    @ApiModelProperty(value = "昵称")
    private String nickname;


}
