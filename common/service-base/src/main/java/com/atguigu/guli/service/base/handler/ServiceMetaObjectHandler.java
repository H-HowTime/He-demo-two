package com.atguigu.guli.service.base.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author hehao
 * @create 2020-12-15 23:44
 */
@Component //将该类扫描到容器中
public class ServiceMetaObjectHandler implements MetaObjectHandler {
    //自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        //插入时自动填充
        metaObject.setValue("gmtCreate",new Date());
        metaObject.setValue("gmtModified",new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //更新是自动填充
        metaObject.setValue("gmtModified",new Date());
    }
}
