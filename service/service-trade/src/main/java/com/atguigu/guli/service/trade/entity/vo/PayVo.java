package com.atguigu.guli.service.trade.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author hehao
 * @create 2021-01-05 19:42
 */
@Data
@Component
public class PayVo {

    @ApiModelProperty(value = "课程ID")
    private String courseId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "订单金额（分）")
    private Long totalFee;

    @ApiModelProperty(value = "二维码链接")
    private String codeUrl;

    @ApiModelProperty(value = "响应码")
    private String resultCode;


}
