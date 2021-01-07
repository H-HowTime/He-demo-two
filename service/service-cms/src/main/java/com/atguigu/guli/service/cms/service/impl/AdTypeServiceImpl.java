package com.atguigu.guli.service.cms.service.impl;

import com.atguigu.guli.service.cms.entity.AdType;
import com.atguigu.guli.service.cms.mapper.AdTypeMapper;
import com.atguigu.guli.service.cms.service.AdTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 推荐位 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-29
 */
@Service
public class AdTypeServiceImpl extends ServiceImpl<AdTypeMapper, AdType> implements AdTypeService {

    @Override
    public Page<AdType> getAdTypePage(Long pageNum, Long pageSize) {
        Page<AdType> page = baseMapper.selectPage(new Page<>(pageNum,pageSize), new QueryWrapper<>());
        return page;
    }
}
