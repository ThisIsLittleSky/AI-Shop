package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.pojo.GoodsCategory;
import com.sky.service.GoodsCategoryService;
import com.sky.mapper.GoodsCategoryMapper;
import com.sky.util.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author SKY
* @description 针对表【goods_category(商品分类表)】的数据库操作Service实现
* @createDate 2026-01-17 09:34:53
*/
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements GoodsCategoryService{



    //商品种类列表查询
    @Override
    public Result<List> getCategoryList() {
        LambdaQueryWrapper<GoodsCategory> queryWrapper = new LambdaQueryWrapper<>();
        // 删除状态
        queryWrapper.eq(GoodsCategory::getDeleted,0);
        // 启用状态
        queryWrapper.eq(GoodsCategory::getStatus, 1);
        queryWrapper.orderByAsc(GoodsCategory::getSort);
        queryWrapper.orderByAsc(GoodsCategory::getId);
        return Result.success(this.list(queryWrapper));
    }
}




