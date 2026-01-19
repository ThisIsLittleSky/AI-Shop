package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.GoodsListResponse;
import com.sky.dto.GoodsPageQuery;
import com.sky.pojo.Goods;
import com.sky.pojo.GoodsCategory;
import com.sky.service.GoodsCategoryService;
import com.sky.service.GoodsService;
import com.sky.mapper.GoodsMapper;
import com.sky.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
* @author SKY
* @description 针对表【goods(商品表)】的数据库操作Service实现
* @createDate 2026-01-17 09:34:53
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @Override
    public Result<IPage<GoodsListResponse>> getGoodsList(GoodsPageQuery query) {
        //创建分类对象，里面传当前页码和每页数量
        Page<Goods> page = new Page<>(query.getCurrent(), query.getSize());
        //构建查询条件
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        //未删除
        queryWrapper.eq(Goods::getDeleted,0);
        //按分类筛选(先判断是不是空)
        if(query.getCategoryId()!=null){
            queryWrapper.eq(Goods::getCategoryId,query.getCategoryId());
        }
        //按状态筛选(1-启用，0-禁用)
        if(query.getStatus()!=null){
            queryWrapper.eq(Goods::getStatus,query.getStatus());
        }else{
            // 默认查询启用状态的商品
            queryWrapper.eq(Goods::getStatus,1);
        }
        //关键词检索商品名称或者描述
        if(StringUtils.hasText(query.getKeyword())){
            queryWrapper.and(a ->
                    a.like(Goods::getDescription,query.getKeyword())
                    .or()
                    .like(Goods::getName,query.getKeyword())
            );
        }
        //按时间倒序
        queryWrapper.orderByDesc(Goods::getUpdateTime);
        // 执行查询
        //page是当前页码和每页数量，queryWrapper是查询条件
        IPage<Goods> goodsPage = this.page(page, queryWrapper);

        //实现 “分类 ID -> 分类名称” 的键值对映射
        // 方便后续通过分类 ID 快速查询对应的分类名称（无需再遍历列表，提升查询效率）。
        //.list()是IService自带的方法
        List<GoodsCategory> categories = goodsCategoryService.list();
        Map<Long,String> categoryMap = new HashMap<>();
        for (GoodsCategory gc : categories) {
            categoryMap.put(gc.getId(),gc.getName());
        }

        //将「商品实体分页对象」IPage<Goods> 转换为「商品列表响应 DTO 分页对象」IPage<GoodsListResponse>
        //1.传入分页查询结果的数据（当前页与总页数）
        IPage<GoodsListResponse> goodsListResponseIPage = new Page<>(
                goodsPage.getCurrent(),
                goodsPage.getSize()
        );
        goodsListResponseIPage.setTotal(goodsPage.getTotal());
        goodsListResponseIPage.setPages(goodsPage.getPages());
        //2.创建空列表，用于储存DTO
        List<GoodsListResponse> goodsList = new ArrayList<>();
        //3.用 for 循环遍历原分页的 records 列表.getRecords是查询结果
        for(Goods goods : goodsPage.getRecords()){
            //空对象用于复制元素
            GoodsListResponse goodsListResponse = new GoodsListResponse();
            BeanUtils.copyProperties(goods,goodsListResponse);
            String categoryName = categoryMap.getOrDefault(goods.getCategoryId(),"未知类型");
            goodsListResponse.setCategoryName(categoryName);
            goodsList.add(goodsListResponse);
        }
        //4.将结果插入新的分页数据中
        //为什么要创建两个分页对象（IPage<Goods> 和 IPage<GoodsListResponse>），核心原因是 「职责隔离」和「类型不兼容」
        //泛型 T=Goods，records 列表存储的是 后端数据库实体 Goods；
        goodsListResponseIPage.setRecords(goodsList);

        return Result.success(goodsListResponseIPage);
    }

    @Override
    public Result<GoodsListResponse> getGoodsDetail(Long id) {
        // 查询商品
        Goods goods = this.getById(id);
        //检查查询结果
        if(goods==null||goods.getDeleted()==1){
            return Result.error(404,"未找到哦，试试搜索其他吧");
        }
        //复制给DTO
        GoodsListResponse goodsListResponse = new GoodsListResponse();
        BeanUtils.copyProperties(goods,goodsListResponse);
        //获得分类名称
        GoodsCategory category = goodsCategoryService.getById(goods.getCategoryId());
        goodsListResponse.setCategoryName(category.getName());

        return Result.success(goodsListResponse);
    }


}




