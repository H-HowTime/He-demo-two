package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.feign.VodFeignClient;
import com.atguigu.guli.service.edu.mapper.ChapterMapper;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Service
@Transactional
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VodFeignClient vodFeignClient;

    @Override
    public List<ChapterVo> getChapterNestedList(String courseId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        // TODO 顺序有小bug
        queryWrapper.orderByAsc("ch.sort", "vi.sort");
        queryWrapper.eq("ch.course_id", courseId);
        return baseMapper.getChapterNestedList(queryWrapper);
    }

    @Override
    public boolean deleteChapterById(String chapterId) {
        //删除章节之前，先删除阿里云中的视频
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("chapter_id", chapterId);
        queryWrapper.select("video_source_id");
        List list = videoMapper.selectObjs(queryWrapper);
        vodFeignClient.deleteVideoList(list);
        //删除视频成功后，删除课时表里的数据
        videoMapper.delete(queryWrapper);
        //最后删除章节信息
        baseMapper.deleteById(chapterId);
        return true;
    }
}
