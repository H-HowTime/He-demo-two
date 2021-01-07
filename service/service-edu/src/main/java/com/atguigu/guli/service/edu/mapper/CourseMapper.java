package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CourseDetailApiVo;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseViewObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
public interface CourseMapper extends BaseMapper<Course> {

    List<CourseViewObject> courseInfoList();

    List<CourseViewObject> courseInfoPageList(
            //mp会自动组装分页参数
            Page<CourseViewObject> courseViewObjectPage,
            //mp会自动组装queryWrapper：
            //@Param(Constants.WRAPPER) 和 xml文件中的 ${ew.customSqlSegment} 对应
            @Param(Constants.WRAPPER) QueryWrapper<CourseViewObject> queryWrapper);

    CoursePublishVo getChapterNestedList(
            @Param("ew") QueryWrapper queryWrapper);

    CourseDetailApiVo courseDetailById(String courseId);

}
