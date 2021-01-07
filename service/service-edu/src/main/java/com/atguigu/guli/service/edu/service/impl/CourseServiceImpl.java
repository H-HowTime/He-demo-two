package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.edu.entity.*;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.ApiCourseQuery;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CourseDetailApiVo;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseViewObject;
import com.atguigu.guli.service.edu.mapper.*;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Transactional //指定当前类的所有方法支持事务 条件是必须已经启用声明时事务
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionMapper descriptionMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        Course course = new Course();
        //使用spring框架中的工具类BeanUtils将一个对象中的值复制到目标对象中
        //要求源对象和目标对象的属性名和数据类型要一致
        BeanUtils.copyProperties(courseInfoForm, course);
        course.setStatus("Draft"); //设置状态Draft为草稿状态
        baseMapper.insert(course); //添加成功会自动将id值返回
        //添加基础课程成功后，添加课程简介
        CourseDescription description = new CourseDescription();
        description.setId(course.getId());
        description.setDescription(courseInfoForm.getDescription());
        descriptionMapper.insert(description);
        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoById(String id) {
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        //1、去course表中查询
        Course course = baseMapper.selectById(id);
        if (course == null) {
            return null;
        }
        BeanUtils.copyProperties(course, courseInfoForm);
        //2、去description表中查询
        CourseDescription description = descriptionMapper.selectById(id);
        if (description != null) {
            courseInfoForm.setDescription(description.getDescription());
        }
        return courseInfoForm;
    }

    @Override
    public Page<CourseViewObject> courseInfoPageList(Long pageNum, Long pageSize, CourseQuery courseQuery) {
        //获取查询条件
        String subjectId = courseQuery.getSubjectId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String teacherId = courseQuery.getTeacherId();
        String title = courseQuery.getTitle();
        QueryWrapper<CourseViewObject> queryWrapper = new QueryWrapper<>();
        //以创建时间排序(降序)
        queryWrapper.orderByDesc("co.gmt_create");
        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.eq("co.subject_id", subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.eq("co.subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            queryWrapper.eq("co.teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("co.title", title);
        }
        Page<CourseViewObject> courseViewObjectPage = new Page<>(pageNum, pageSize);
        //放入分页参数和查询条件 mbp会自动组装
        List<CourseViewObject> courseViewObjectList = baseMapper.courseInfoPageList(courseViewObjectPage, queryWrapper);
        courseViewObjectPage.setRecords(courseViewObjectList);
        return courseViewObjectPage;
    }

    @Override
    public List<CourseViewObject> courseInfoList() {
        return baseMapper.courseInfoList();
    }

    @Override
    public void updateCourseIfo(CourseInfoForm courseInfoForm, String courseId) {
        //获取课程简介
        String description = courseInfoForm.getDescription();
        //将courseInfoForm中的数据复制到course中
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);
        course.setId(courseId);
        //更新课程数据
        baseMapper.updateById(course);

        //将courseInfoForm中的数据设置到到description中
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(courseId);
        courseDescription.setDescription(description);
        //更新课程简介
        int i = descriptionMapper.updateById(courseDescription);
        if (i == 0) {
            //如果之前没有课程简介，则执行添加操作
            descriptionMapper.insert(courseDescription);
        }
    }

    @Override
    public CoursePublishVo coursePublish(String courseId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("co.id", courseId);
        return baseMapper.getChapterNestedList(queryWrapper);
    }

    @Override
    public boolean changeCourseStatusById(String courseId) {
        Course course = new Course();
        course.setId(courseId);
        UpdateWrapper<Course> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", courseId);
        updateWrapper.set("status", "Normal");
        updateWrapper.set("publish_time", new Date());
        int update = baseMapper.update(course, updateWrapper);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCourseById(String courseId) {
        //课时信息：video
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", courseId);
        videoMapper.delete(videoQueryWrapper);
        //章节信息：chapter
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", courseId);
        chapterMapper.delete(chapterQueryWrapper);
        //课程详情：course_description
        descriptionMapper.deleteById(courseId);
        //课程信息：course
        int i = baseMapper.deleteById(courseId);
        if (i > 0) {
            return true;
        }
        return false;
    }

    @Override
    public String removeCoverById(String courseId) {
        //根据课程id获取封面图片地址
        Course course = baseMapper.selectById(courseId);
        return course.getCover();
    }

    @Override
    public List<String> getVideoIds(String courseId) {
        // 根据课程id删除视频--课程id查询去video表中视频资源video_source_id
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.select("video_source_id");
        queryWrapper.isNotNull("video_source_id");
        // TODO 当对应的video没有视频时，[ff83358adb024868807a0f0f99f146d7, 0cc5e42e03d5474b9e4d8ebd95feeb7e, ]
        List list = videoMapper.selectObjs(queryWrapper);
        return list;
    }

    @Override
    public List<Course> getCourseByTid(String tid) {
        //根据讲师id获取课程信息
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("teacher_id", tid);
        List<Course> courses = baseMapper.selectList(courseQueryWrapper);
        return courses;
    }

    @Override
    public List<Course> courseList(ApiCourseQuery apiCourseQuery) {
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        //查询已经发布的课程
//        courseQueryWrapper.eq("status", "Normal");
        if (apiCourseQuery != null) {
            //按一级分类查询
            if (!StringUtils.isEmpty(apiCourseQuery.getSubjectParentId())) {
                courseQueryWrapper.eq("subject_parent_id", apiCourseQuery.getSubjectParentId());
            }
            //按二级分类查询
            if (!StringUtils.isEmpty(apiCourseQuery.getSubjectId())) {
                courseQueryWrapper.eq("subject_id", apiCourseQuery.getSubjectId());
            }
            //按销量排序
            if (!StringUtils.isEmpty(apiCourseQuery.getBuyCountSort())) {
                courseQueryWrapper.orderByDesc("buy_count");
            }
            //按发布时间排序
            if (!StringUtils.isEmpty(apiCourseQuery.getPublishTimeSort())) {
                courseQueryWrapper.orderByDesc("publish_time");
            }
            //按价格排序
            if (apiCourseQuery.getPriceSortType() == null || apiCourseQuery.getPriceSortType() == 1) {
                courseQueryWrapper.orderByAsc("price");
            } else {
                courseQueryWrapper.orderByDesc("price");
            }

        }
        List<Course> courses = baseMapper.selectList(courseQueryWrapper);
        return courses;
    }

    @Override
    public CourseDetailApiVo courseDetailById(String courseId) {
        //更新课程的浏览数量
        Course course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);
        return baseMapper.courseDetailById(courseId);
    }

    @Override
    public CourseDto courseOrderById(String courseId) {
        //获取课程信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id",courseId);
        queryWrapper.select("id","teacher_id","title","cover","price");
        Course course = baseMapper.selectOne(queryWrapper);
        //获取讲师姓名
        String teacherId = course.getTeacherId();
        QueryWrapper teacherQuery = new QueryWrapper();
        teacherQuery.eq("id",teacherId);
        teacherQuery.select("name");
        Teacher teacher = teacherMapper.selectOne(teacherQuery);
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setCover(course.getCover());
        courseDto.setPrice(course.getPrice());
        courseDto.setTeacherId(course.getTeacherId());
        courseDto.setTeacherName(teacher.getName());
        courseDto.setTitle(course.getTitle());
        return courseDto;
    }

    @Override
    public boolean updateCourse2BuyCount(String courseId) {
        //获取订单支付的课程信息
        Course course = baseMapper.selectById(courseId);
        if(course == null){
            throw new GuliException(ResultCodeEnum.REMOTE_REQUEST_ERROR);
        }
        //更新课程销量
        course.setBuyCount(course.getBuyCount() + 1);
        int update = baseMapper.updateById(course);
        if(update > 0){
            return true;
        }
        return false;
    }
}
