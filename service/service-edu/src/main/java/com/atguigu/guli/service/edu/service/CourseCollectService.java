package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.CourseCollect;
import com.atguigu.guli.service.edu.entity.vo.CollectCourseVo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 课程收藏 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
public interface CourseCollectService extends IService<CourseCollect> {

    boolean isCollectCourse(String courseId, String memberId);

    void saveCollectCourse(String courseId, String memberId);

    List<CollectCourseVo> getCollectCourseList(String memberId);

    void deleteCollectCourse(String courseId, String memberId);
}
