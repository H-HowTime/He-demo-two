package com.atguigu.guli.service.edu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.atguigu.guli.service.base.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程简介
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_course_description")
@ApiModel(value="CourseDescription对象", description="课程简介")
public class CourseDescription extends BaseEntity {

    private static final long serialVersionUID=1L;
    //CourseDescription id值添加时使用课程id填充 在此声明会覆盖掉父类的主键策略雪花算法
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.NONE)
    //value表示当前属性映射到数据库中的哪个字段
    private String id;

    @ApiModelProperty(value = "课程简介")
    private String description;


}
