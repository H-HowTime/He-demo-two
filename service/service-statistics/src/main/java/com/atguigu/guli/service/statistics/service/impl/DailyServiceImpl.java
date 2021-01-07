package com.atguigu.guli.service.statistics.service.impl;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.statistics.entity.Daily;
import com.atguigu.guli.service.statistics.feign.MemberFeignClient;
import com.atguigu.guli.service.statistics.mapper.DailyMapper;
import com.atguigu.guli.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2021-01-06
 */
@Slf4j
@Transactional
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    private MemberFeignClient memberFeignClient;

    @Override
    public void createStatisticsByDay(String dateDay) {
        //判断当日记录是否以存在，删除重新录入
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated", dateDay);
        baseMapper.delete(queryWrapper);

        //获取统计数据
        //远程调用获取用户注册数量
        R memberNumR = memberFeignClient.getMemberNum(dateDay);
        Integer memberNum = (Integer) memberNumR.getData().get("memberNum");
        Integer courseNum = RandomUtils.nextInt(100, 200);
        Integer loginNum = RandomUtils.nextInt(100, 200);
        Integer videoViewNum = RandomUtils.nextInt(100, 200);

        //添加统计数据
        Daily daily = new Daily();
        daily.setRegisterNum(memberNum);
        daily.setCourseNum(courseNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setDateCalculated(dateDay);
        baseMapper.insert(daily);
    }

    @Override
    public Map<String, Object> showChart(String begin, String end) {
        /*
        查询统计信息，返回一个map
            map:
              xDate: list(时间)
              yRegisterNum: list(注册数量)
              yLoginNum: list(登录数量)
              yVideoViewNum: list(视频观看数量)
              yCourseNum: list(课程数量)
         */
        //查询统计数据
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("date_calculated", begin, end);
        List<Daily> dailyList = baseMapper.selectList(queryWrapper);
        List<String> xDate = new ArrayList<>();
        List<Integer> yRegisterNum = new ArrayList<>();
        List<Integer> yLoginNum = new ArrayList<>();
        List<Integer> yVideoViewNum = new ArrayList<>();
        List<Integer> yCourseNum = new ArrayList<>();

        for (Daily daily : dailyList) {
            String dateCalculated = daily.getDateCalculated();
            xDate.add(dateCalculated);
            Integer registerNum = daily.getRegisterNum();
            yRegisterNum.add(registerNum);
            Integer loginNum = daily.getLoginNum();
            yLoginNum.add(loginNum);
            Integer videoViewNum = daily.getVideoViewNum();
            yVideoViewNum.add(videoViewNum);
            Integer courseNum = daily.getCourseNum();
            yCourseNum.add(courseNum);
        }
        Map<String, Object> showCharMap = new HashMap<>();
        showCharMap.put("xDate", xDate);
        showCharMap.put("yRegisterNum", yRegisterNum);
        showCharMap.put("yLoginNum", yLoginNum);
        showCharMap.put("yVideoViewNum", yVideoViewNum);
        showCharMap.put("yCourseNum", yCourseNum);

        return showCharMap;
    }

    //实现定时任务，每天凌晨1点执行创建统计前一天的数据 TODO 研究一下spring定时任务
    @Scheduled(cron = "0 0 1 * * ? ") //spring的定时器只支持六位
    public void executeCreateStatistics() {
        String day = new DateTime().minusDays(1).toString("yyyy-MM-dd");
        createStatisticsByDay(day);
        log.info("executeCreateStatistics方法 统计完毕");
    }
}
