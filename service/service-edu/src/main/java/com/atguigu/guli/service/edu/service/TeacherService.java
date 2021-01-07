package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.entity.vo.TeacherViewObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
public interface TeacherService extends IService<Teacher> {

    Page<Teacher> pageByTeacherQuery(Integer pageNum, Integer pageSize, TeacherQuery teacherQuery);

    TeacherViewObject getVOById(String id);

    List<Map<String, String>> getTeacherName();
}
