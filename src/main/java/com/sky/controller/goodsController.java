package com.sky.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sky.dto.GoodsListResponse;
import com.sky.dto.GoodsPageQuery;
import com.sky.service.GoodsCategoryService;
import com.sky.service.GoodsService;
import com.sky.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Tool:Intellij IDEA
 * @AUthor:周子天
 * @Date:2026-01-18-下午4:31
 * @Version:1.0
 * @Description:商品
 */
@RestController
@RequestMapping("/goods")
public class goodsController {


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsCategoryService goodsCategoryService;

    /**
     * 获取商品分类列表
     * GET /api/goods/categories
     */
    @GetMapping("/categories")
    public Result<List> getCategoryList() {
        return goodsCategoryService.getCategoryList();
    }

    /**
     * 获取商品列表（分页）
     * GET /api/goods/list
     * 参数：current=1&size=10&categoryId=1&keyword=手机&status=1
     */
    // TODO 放redis里做缓存
    @GetMapping("/list")
    public Result<IPage<GoodsListResponse>> getGoodsList(GoodsPageQuery query) {
        // 设置默认值
        if (query.getCurrent() == null || query.getCurrent() < 1) {
            query.setCurrent(1);
        }
        if (query.getSize() == null || query.getSize() < 1) {
            query.setSize(10);
        }
        return goodsService.getGoodsList(query);
    }

    /**
     * 获取商品详情
     * GET /api/goods/detail/{id}
     */
    @GetMapping("/detail/{id}")
    //@PathVariable是从路径中获得商品
    public Result<GoodsListResponse> getGoodsDetail(@PathVariable Long id) {
        return goodsService.getGoodsDetail(id);
    }

}
