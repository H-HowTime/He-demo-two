package com.atguigu.guli.service.statistics.service;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author hehao
 * @create 2021-01-06 18:46
 */
@Slf4j
@Service
public class generateDateTest {

//    //1,5,6,10 * * * * * 每分钟的1,5,6,10执行
//    @Scheduled(cron = "1,5,6,10 * * * * *")
//    public void test(){
//        log.info("当前时间：{}",new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
//        //获取明天的日期
//        String yseDay = new DateTime().plusDays(1).toString("yyyy-MM-dd HH:mm:ss");
//        //获取昨天的日期
//        String lastDay = new DateTime().minusDays(1).toString("yyyy-MM-dd HH:mm:ss");
//        log.info("明天：{}",lastDay);
//        log.info("昨天：{}",yseDay);
//    }
}
