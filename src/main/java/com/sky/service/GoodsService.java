package com.sky.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sky.dto.AdminGoodsCreateRequest;
import com.sky.dto.AdminGoodsUpdateRequest;
import com.sky.dto.GoodsListResponse;
import com.sky.dto.GoodsPageQuery;
import com.sky.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.util.Result;

import java.util.List;

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
    //管理员商品获得
    Result<IPage<GoodsListResponse>> getAdminGoodsList(GoodsPageQuery goodsPageQuery);
    //管理员创建商品
    Result<String> createGoods(AdminGoodsCreateRequest request);
    //管理员修改商品
    Result<String> updateGoods(Long id, AdminGoodsUpdateRequest request);
    //管理员删除商品
    Result<String> deleteGoods(Long id);
    //管理员批量删除商品
    Result<String> batchDeleteGoods(List<Long> ids);
}
