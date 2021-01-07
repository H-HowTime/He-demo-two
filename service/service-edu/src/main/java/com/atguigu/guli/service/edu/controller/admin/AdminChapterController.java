package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
//@CrossOrigin  由gateway网关全局路由配置 //解决跨域问题
@Api(tags = "章节管理模块")
@RestController
@RequestMapping("/admin/edu/chapter")
public class AdminChapterController {

    @Autowired
    private ChapterService chapterService;

    //新增章节信息
    @ApiOperation(value = "新增章节信息")
    @PostMapping(value = "/save-chapter")
    public R saveChapter(
            @ApiParam(value = "章节信息") @RequestBody Chapter chapter) {
        boolean b = chapterService.save(chapter);
        if (b) {
            return R.ok().message("新增章节成功");
        }
        return R.error().message("新增章节失败");
    }

    //获取指定章节信息
    @ApiOperation(value = "获取指定章节信息")
    @GetMapping(value = "/get-chapter/{chapterId}")
    public R getChapterById(
            @ApiParam(value = "章节ID") @PathVariable(value = "chapterId") String chapterId) {
        Chapter chapter = chapterService.getById(chapterId);
        if (chapter == null) {
            return R.error().message("获取章节失败");
        }
        return R.ok().message("获取章节成功").data("chapter", chapter);
    }

    //更新章节信息
    @ApiOperation(value = "更新章节信息")
    @PutMapping(value = "/update-chapter")
    public R updateChapter(
            @ApiParam(value = "章节信息") @RequestBody Chapter chapter) {
        boolean b = chapterService.updateById(chapter);
        if (b) {
            return R.ok().message("更新章节成功");
        }
        return R.error().message("更新章节失败");
    }

    //删除章节信息
    @ApiOperation(value = "删除章节信息")
    @DeleteMapping(value = "/delete-chapter/{chapterId}")
    public R deleteChapterById(
            @ApiParam(value = "章节ID") @PathVariable(value = "chapterId") String chapterId) {
        // 已解决，删除章节时批量删除视频
        boolean b = chapterService.deleteChapterById(chapterId);
        if (b) {
            return R.ok().message("删除章节成功");
        }
        return R.error().message("删除章节失败");
    }

    //根据课程ID获取章节嵌套课时信息
    @ApiOperation(value = "根据课程ID获取章节嵌套课时信息")
    @GetMapping(value = "/get-chapter-nested-list/{courseId}")
    public R getChapterNestedList(
            @ApiParam(value = "课程ID") @PathVariable(value = "courseId") String courseId) {
        List<ChapterVo> ChapterNestedList = chapterService.getChapterNestedList(courseId);
        if (ChapterNestedList == null) {
            return R.error().message("根据课程ID获取章节嵌套课时信息成功");
        }
        return R.ok().message("根据课程ID获取章节嵌套课时信息失败").data("ChapterNestedList", ChapterNestedList);
    }
}

