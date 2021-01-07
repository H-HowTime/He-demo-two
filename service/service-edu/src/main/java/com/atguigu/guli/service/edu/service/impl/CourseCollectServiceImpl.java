package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.edu.entity.CourseCollect;
import com.atguigu.guli.service.edu.entity.vo.CollectCourseVo;
import com.atguigu.guli.service.edu.mapper.CourseCollectMapper;
import com.atguigu.guli.service.edu.service.CourseCollectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 课程收藏 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Service
public class CourseCollectServiceImpl extends ServiceImpl<CourseCollectMapper, CourseCollect> implements CourseCollectService {

    @Override
    public boolean isCollectCourse(String courseId, String memberId) {
        QueryWrapper<CourseCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", memberId).eq("course_id", courseId);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void saveCollectCourse(String courseId, String memberId) {
        //判断是否已经收藏
        boolean b = this.isCollectCourse(courseId, memberId);
        if (!b) {
            CourseCollect courseCollect = new CourseCollect();
            courseCollect.setCourseId(courseId);
            courseCollect.setMemberId(memberId);
            baseMapper.insert(courseCollect);
        }
    }

    @Override
    public List<CollectCourseVo> getCollectCourseList(String memberId) {

        return baseMapper.getCollectCourseList(memberId);
    }

    @Override
    public void deleteCollectCourse(String courseId, String memberId) {
        QueryWrapper<CourseCollect> queryWrapper = new QueryWrapper();
        queryWrapper.eq("member_id", memberId).eq("course_id", courseId);
        baseMapper.delete(queryWrapper);
    }
}
