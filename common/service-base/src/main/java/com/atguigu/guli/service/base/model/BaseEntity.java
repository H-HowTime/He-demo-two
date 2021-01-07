package com.atguigu.guli.service.base.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author hehao
 * @create 2020-12-15 18:06
 */
@Data //自动生成get、set方法
//@Accessors用于配置setter方法的生成结果,chain设置为true，则setter方法返回当前对象,可以实现链式操作
@Accessors(chain = true)
public class BaseEntity {
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID) //主键策略 雪花算法
    //value表示当前属性映射到数据库中的哪个字段
    private String id;

//    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss") //返回时间数据时遵循东八区 可以在yml配置文件中配置
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT) //在添加的时候自动填充
    private Date gmtCreate;

//    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss") //返回时间数据时遵循东八区
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE) //在添加和更新的时候自动填充
    private Date gmtModified;
}
