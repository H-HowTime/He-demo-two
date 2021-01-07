package com.atguigu.guli.service.cms.service.impl;

import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.vo.AdVo;
import com.atguigu.guli.service.cms.mapper.AdMapper;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-29
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    /**
     * 需要缓存的业务方法 缓存热门广告数据
     * @Cacheable(value = ,key = ) 一般使用在方法上，缓存的是方法的返回值
     * value表示缓存key拼接的前缀(缓存的位置) key缓存key拼接的后缀（拼接时需要在左右两边+单引号）
     * 拼接的格式为： ad::getAdByTypeId
     *
     * key值可以到动态拼接： “#参数名”或者“#p参数index”
     * @param typeId
     * @return
     */

    @Cacheable(value = "ad",key = "'getAdByTypeId-typeId:' + #typeId")
    @Override
    public List<Ad> getAdByTypeId(String typeId) {
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id", typeId);
        System.out.println("测试");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<AdVo> getAdPage(Long pageNum, Long pageSize) {
        QueryWrapper queryWrapper = new QueryWrapper();
        //按照type-id和sort排序
        queryWrapper.orderByAsc("ad.type_id","ad.sort");
        IPage<AdVo> page = new Page<>(pageNum,pageSize);
        List<AdVo> adVoList = baseMapper.getAdPage(page,queryWrapper);
        page.setRecords(adVoList);
        return page;
    }
}
