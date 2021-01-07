package com.atguigu.guli.service.cms.service;

import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.vo.AdVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-29
 */
public interface AdService extends IService<Ad> {

    List<Ad> getAdByTypeId(String typeId);

    IPage<AdVo> getAdPage(Long pageNum, Long pageSize);
}
