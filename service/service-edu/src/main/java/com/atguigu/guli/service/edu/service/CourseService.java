package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.ApiCourseQuery;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CourseDetailApiVo;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseViewObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
public interface CourseService extends IService<Course> {

    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfoById(String id);

    IPage<CourseViewObject> courseInfoPageList(Long pageNum, Long pageSize, CourseQuery courseQuery);

    List<CourseViewObject> courseInfoList();

    void updateCourseIfo(CourseInfoForm courseInfoForm, String courseId);

    CoursePublishVo coursePublish(String courseId);

    boolean changeCourseStatusById(String courseId);

    boolean deleteCourseById(String courseId);

    String removeCoverById(String courseId);

    List<String> getVideoIds(String courseId);

    List<Course> getCourseByTid(String tid);

    List<Course> courseList(ApiCourseQuery apiCourseQuery);

    CourseDetailApiVo courseDetailById(String courseId);

    CourseDto courseOrderById(String courseId);

    boolean updateCourse2BuyCount(String courseId);
}
