package com.sky.service;

import com.sky.dto.AdminCategoryCreateRequest;
import com.sky.dto.AdminCategoryUpdateRequest;
import com.sky.pojo.GoodsCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.util.Result;

import java.util.List;

/**
* @author SKY
* @description 针对表【goods_category(商品分类表)】的数据库操作Service
* @createDate 2026-01-17 09:34:53
*/
public interface GoodsCategoryService extends IService<GoodsCategory> {
    //商品分类列表查询
    Result<List> getCategoryList();
    //管理员获取所有商品分类
    Result<List<GoodsCategory>> getAllCategories();
    //管理员创建商品分类
    Result<String> createCategory(AdminCategoryCreateRequest request);
    //管理员修改商品分类
    Result<String> updateCategory(Long id, AdminCategoryUpdateRequest request);
    //管理删除商品
    Result<String> deleteCategory(Long id);
}
