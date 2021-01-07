package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.vo.SubjectViewObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
public interface SubjectMapper extends BaseMapper<Subject> {
     List<SubjectViewObject> SubjectNestedList();

}
