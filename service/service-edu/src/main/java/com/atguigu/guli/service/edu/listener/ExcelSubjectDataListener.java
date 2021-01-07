package com.atguigu.guli.service.edu.listener;

import ch.qos.logback.classic.Logger;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hehao
 * @create 2020-12-22 22:47
 */
@Slf4j
//@Component //将该监听器扫描到容器中
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    private SubjectMapper subjectMapper;

    public ExcelSubjectDataListener(SubjectMapper subjectMapper){
        this.subjectMapper = subjectMapper;
    }

    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        String levelOneTitle = excelSubjectData.getLevelOneTitle();
        String levelTwoTitle = excelSubjectData.getLevelTwoTitle();
        //将解析出来的数据保存到数据库中
        //验证一级分类是否第一个存入数据库
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", levelOneTitle);
        queryWrapper.eq("parent_id", 0);
        Subject levelOneSubject = subjectMapper.selectOne(queryWrapper);
        if (levelOneSubject == null) {
            //第一次加入
            //将一级分类标题转化一级课程分类对象存入数据库
            levelOneSubject = new Subject();
            levelOneSubject.setTitle(levelOneTitle);
            levelOneSubject.setParentId("0");
            subjectMapper.insert(levelOneSubject); //默认会将新增数据的id设置给当前Javabean
        }
        //验证二级分类是否第二个存入数据库
        queryWrapper.clear();
        queryWrapper.eq("title", levelTwoTitle);
        queryWrapper.eq("parent_id", levelOneSubject.getId());
        Subject levelTwoSubject = subjectMapper.selectOne(queryWrapper);
        if (levelTwoSubject == null) {
            //将二级分类标题转化二级课程分类对象存入数据库
            levelTwoSubject = new Subject();
            levelTwoSubject.setTitle(levelTwoTitle);
            levelTwoSubject.setParentId(levelOneSubject.getId());
            subjectMapper.insert(levelTwoSubject);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("数据全部解析完成了");
    }
}
