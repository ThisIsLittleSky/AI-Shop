package com.sky.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sky.dto.GoodsListResponse;
import com.sky.dto.GoodsPageQuery;
import com.sky.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.util.Result;

/**
* @author SKY
* @description 针对表【goods(商品表)】的数据库操作Service
* @createDate 2026-01-17 09:34:53
*/
public interface GoodsService extends IService<Goods> {

    //商品列表分页查询
    Result<IPage<GoodsListResponse>> getGoodsList(GoodsPageQuery query);
    //获得商品详情
    Result<GoodsListResponse> getGoodsDetail(Long id);

}
