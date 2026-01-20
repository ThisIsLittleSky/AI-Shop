package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.AdminGoodsCreateRequest;
import com.sky.dto.AdminGoodsUpdateRequest;
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


    //管理员商品获得
    @Override
    public Result<IPage<GoodsListResponse>> getAdminGoodsList(GoodsPageQuery goodsPageQuery) {
        //1.创建分页对象
        Page<Goods> page = new Page<>(goodsPageQuery.getCurrent(), goodsPageQuery.getSize());
        //2.构建查询条件
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        //3.筛选没有被删除的
        queryWrapper.eq(Goods::getDeleted,0);
        //4.如果加了分类条件就要查分类条件
        if(goodsPageQuery.getCategoryId()!=null){
            queryWrapper.eq(Goods::getCategoryId,goodsPageQuery.getCategoryId());
        }
        //5.如果加了状态就要查状态
        if(goodsPageQuery.getStatus()!=null){
            queryWrapper.eq(Goods::getStatus,goodsPageQuery.getStatus());
        }
        //6.如果加了关键词就要查关键词
        if(StringUtils.hasText(goodsPageQuery.getKeyword())){
            queryWrapper.and(
                    a -> a.like(Goods::getDescription,goodsPageQuery.getKeyword())
            ).or().like(Goods::getName,goodsPageQuery.getKeyword());
        }
        //7.按时间倒序排
        queryWrapper.orderByDesc(Goods::getUpdateTime);
        //8.分页查询开始
        IPage<Goods> goodsPage = this.page(page, queryWrapper);
        //9.构建商品种类列表
        List<GoodsCategory> categories = goodsCategoryService.list();
        //10.用hash存，后续再匹配就从hashmap里读
        Map<Long,String> categoryMap = new HashMap<>();
        for (GoodsCategory gc : categories) {
            categoryMap.put(gc.getId(),gc.getName());
        }
        //11.构建分页响应(与上面的分页响应一样，只不过对象不一样，第一次是POJO，这一次是DTO)
        IPage<GoodsListResponse> goodsListResponseIPage = new Page<>(
                goodsPage.getCurrent(),
                goodsPage.getSize()
        );
        goodsListResponseIPage.setTotal(goodsPage.getTotal());
        goodsListResponseIPage.setPages(goodsPage.getPages());
        //12.新建一个内容响应列表
        List<GoodsListResponse> goodsList = new ArrayList<>();
        //13.遍历POJO的分页数据
        for(Goods goods : goodsPage.getRecords()){
            //把POJO的分页数据复制给DTO
            GoodsListResponse goodsListResponse = new GoodsListResponse();
            BeanUtils.copyProperties(goods,goodsListResponse);
            //查一下对应分类ID的名称，并添加进DTO的响应里
            String categoryName = categoryMap.get(goods.getCategoryId());
            goodsListResponse.setCategoryName(categoryName);
            goodsList.add(goodsListResponse);
        }
        //14.设置DTO的响应内容列表
        goodsListResponseIPage.setRecords(goodsList);

        return Result.success(goodsListResponseIPage);
    }

    //管理员创建商品
    @Override
    public Result<String> createGoods(AdminGoodsCreateRequest request) {
        //1.商品分类是否存在
        GoodsCategory category = goodsCategoryService.getById(request.getCategoryId());
        //2.商品数据不能为空
        if(category == null || category.getDeleted() == 1){return Result.error(400, "商品分类不存在");}
        if(StringUtils.isEmpty(request.getName())){return Result.error(400,"商品名称不能为空");}
        if(StringUtils.isEmpty(request.getDescription())){return Result.error(400,"商品描述不能为空");}
        if(StringUtils.isEmpty(request.getPrice())){return Result.error(400,"商品价格不能为空");}
        if(StringUtils.isEmpty(request.getStock())){return Result.error(400,"商品数量不能为空");}
        if(StringUtils.isEmpty(request.getImage())){return Result.error(400,"商品图片不能为空");}
        //3.创建POJO
        Goods goods = new Goods();
        //4.DTO数据复制给POJO
        BeanUtils.copyProperties(request,goods);
        // 5.如果没有设置状态，默认为下架
        if (goods.getStatus() == null) {
            goods.setStatus(0);
        }
        //6.保存商品
        boolean success = this.save(goods);
        if (success) {
            return Result.success("商品创建成功");
        } else {
            return Result.error(500, "商品创建失败");
        }
    }

    //修改商品信息
    //根据传的ID查POJO，再验证DTO的分类是不是合法，如果合法就是给POJO，再更新其他信息，更新完用updateById(POJO)修改
    @Override
    public Result<String> updateGoods(Long id, AdminGoodsUpdateRequest request) {
        //1.查商品是否存在
        Goods goods = this.getById(id);
        if(goods==null||goods.getDeleted()==1){
            return Result.error(404,"商品不存在");
        }
        //2.查商品分类是否存在
        if(request.getCategoryId()!=null){
            GoodsCategory category = goodsCategoryService.getById(request.getCategoryId());
            if(category.getDeleted()==1||category==null){
                return Result.error(400, "商品分类不存在");
            }
            goods.setCategoryId(request.getCategoryId());
        }
        // 3.更新商品信息
        if (StringUtils.hasText(request.getName())) {
            goods.setName(request.getName());
        }
        if (request.getPrice() != null) {
            goods.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            goods.setStock(request.getStock());
        }
        if (StringUtils.hasText(request.getImage())) {
            goods.setImage(request.getImage());
        }
        if (StringUtils.hasText(request.getDescription())) {
            goods.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            goods.setStatus(request.getStatus());
        }
        //4.用this.updateById()更新
        boolean success = this.updateById(goods);
        if (success) {
            return Result.success("商品更新成功");
        }else{
            return Result.error(500, "商品更新失败");
        }


    }

    //管理员删除商品
    @Override
    public Result<String> deleteGoods(Long id) {
        //1.查询商品
        Goods goods = this.getById(id);
        if(goods==null||goods.getDeleted()==1){
            return Result.error(400,"商品不存在");
        }
        //2.逻辑删除在yml已经配置，直接用removeById
        //goods.setDeleted(1);
        //3.应用修改
        boolean success = this.removeById(goods.getId());
        if (success) {
            return Result.success("删除成功");
        }else{
            return Result.error("删除失败");
        }

    }

    //管理员批量删除商品
    @Override
    public Result<String> batchDeleteGoods(List<Long> ids) {
        //1.先判断列表是不是空
        if (ids == null || ids.size() == 0) {
            return Result.error(400,"列表为空");
        }
        //2.构建查询条件
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Goods::getId, ids);
        queryWrapper.eq(Goods::getDeleted,0);
        //3.用把查询结果的goods对象变成列表
        List<Goods> goodsList = this.list(queryWrapper);
        //4.逻辑删除在yml已经配置，直接用removeById
        /*for (Goods goods : goodsList) {
            goods.setDeleted(1);
        }*/
        //5.应用修改
        boolean success = this.removeBatchByIds(goodsList);
        if (success) {
            return Result.success("批量删除成功");
        }else{
            return Result.error(500, "批量删除失败");
        }
    }


}




