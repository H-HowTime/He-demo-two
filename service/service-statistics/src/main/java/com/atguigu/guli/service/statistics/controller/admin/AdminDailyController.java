package com.atguigu.guli.service.statistics.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2021-01-06
 */
@Api(tags = "后台统计管理模块")
@RestController
@RequestMapping("/admin/statistics/daily")
public class AdminDailyController {

    @Autowired
    private DailyService dailyService;

    //1、生成统计数据
    @ApiOperation(value = "生成统计数据")
    @PostMapping(value = "/create-statistics/{dateDay}")
    public R createStatisticsByDay(
            @ApiParam(value = "注册时间") @PathVariable(value = "dateDay") String dateDay) {
        dailyService.createStatisticsByDay(dateDay);
        return R.ok().message("生成统计数据成功");
    }
    //2、查询统计信息
    @ApiOperation(value = "查询统计信息")
    @GetMapping(value = "/show-Chart/{begin}/{end}")
    public R showChart(
            @ApiParam(value = "起始时间") @PathVariable(value = "begin") String begin,
            @ApiParam(value = "终止时间") @PathVariable(value = "end") String end) {
        Map<String,Object> showCharMap = dailyService.showChart(begin,end);
        return R.ok().message("查询统计信息成功").data(showCharMap);
    }
}

