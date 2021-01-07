package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.entity.vo.TeacherViewObject;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public Page<Teacher> pageByTeacherQuery(Integer pageNum, Integer pageSize, TeacherQuery teacherQuery) {
        Page<Teacher> page = new Page<>(pageNum,pageSize);
        if(teacherQuery == null){
            return baseMapper.selectPage(page,null);
        }
        //讲师姓名
        String name = teacherQuery.getName();
        //讲师头衔
        Integer level = teacherQuery.getLevel();
        //开始时间
        String joinDateBegin = teacherQuery.getJoinDateBegin();
        //终止时间
        String joinDateEnd = teacherQuery.getJoinDateEnd();

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
            queryWrapper.likeRight("name",name);
        }
        if(level != null){
            queryWrapper.eq("level",level);
        }
        //当数据库中有时间类型的数据时，长度相同时可以直接使用字符串进行比较
        //长度不一样时，可以使用where DATE(时间类型的字段) = ‘2019-03-06’
        //sql中如何判断时间日期，如果传入的时间日期格式和数据库中保存的数据格式一样，可以直接使用字符串的方式进行比较
        if(!StringUtils.isEmpty(joinDateBegin)){
            queryWrapper.ge("join_date",joinDateBegin);
        }
        if(!StringUtils.isEmpty(joinDateEnd)){
            queryWrapper.le("join_date",joinDateEnd);
        }
        return  baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public TeacherViewObject getVOById(String id) {
        Teacher teacher = baseMapper.selectById(id);
        TeacherViewObject viewObject = new TeacherViewObject(teacher.getId(),
                teacher.getName(),teacher.getLevel(),teacher.getSort(),teacher.getJoinDate(),
                teacher.getCareer(),teacher.getIntro(),teacher.getAvatar());
        return viewObject;
    }

    @Override
    public List<Map<String, String>> getTeacherName() {
        List<Teacher> teachers = baseMapper.selectList(null);

        System.out.println("teachers = " + teachers);
        List<Map<String,String>> list = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        for (Teacher teacher : teachers) {
            String name = teacher.getName();
            map.put("value",name);
        }
        list.add(map);
        return list;
    }
}
