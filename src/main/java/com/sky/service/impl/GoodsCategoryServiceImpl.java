package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.AdminCategoryCreateRequest;
import com.sky.dto.AdminCategoryUpdateRequest;
import com.sky.pojo.GoodsCategory;
import com.sky.service.GoodsCategoryService;
import com.sky.mapper.GoodsCategoryMapper;
import com.sky.util.Result;
import org.springframework.beans.BeanUtils;
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

    //获得所有商品种类
    @Override
    public Result<List<GoodsCategory>> getAllCategories() {
        //1.构建查询条件
        LambdaQueryWrapper<GoodsCategory> queryWrapper = new LambdaQueryWrapper<>();
        //2.剔除逻辑删除
        queryWrapper.eq(GoodsCategory::getDeleted,0);
        //3.执行查询
        List<GoodsCategory> list = this.list(queryWrapper);
        return Result.success(list);
    }

    //新建商品分类
    @Override
    public Result<String> createCategory(AdminCategoryCreateRequest request) {
        if(request.getParentId()!=0&&request.getParentId()!=0){
            GoodsCategory goodsCategory = this.getById(request.getParentId());
            if(goodsCategory==null){
                return Result.error("父类不存在");
            }
        }
        GoodsCategory category = new GoodsCategory();
        BeanUtils.copyProperties(request,category);
        // 如果没有设置父分类，默认为0（顶级分类）
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        // 如果没有设置排序，默认为0
        if (category.getSort() == null) {
            category.setSort(0);
        }
        // 如果没有设置状态，默认为启用
        if (category.getStatus() == null) {
            category.setStatus(1);
        }

        boolean save = this.save(category);
        if(save) {
            return Result.success("新建成功");
        }
        return Result.error("新建失败");

    }
    //管理员修改商品分类
    @Override
    public Result<String> updateCategory(Long id, AdminCategoryUpdateRequest request) {
        //1.分类ID是否存在
        GoodsCategory goodsCategory = this.getById(id);
        if(goodsCategory==null||goodsCategory.getDeleted()==1){
            return Result.error(404,"商品种类不存在");
        }
        // 2.验证父分类是否存在（不能设置自己为父分类）
        if (request.getParentId() != null && request.getParentId() > 0) {
            if (request.getParentId().equals(id)) {
                return Result.error(400, "不能将自身设置为父分类");
            }
            //父类是否存在
            GoodsCategory parent = this.getById(request.getParentId());
            if (parent == null || parent.getDeleted() == 1) {
                return Result.error(400, "父分类不存在");
            }
        }
        //3.复制
        BeanUtils.copyProperties(request,goodsCategory);
        boolean update = this.updateById(goodsCategory);
        if(update) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }
    //管理员删除商品分列
    @Override
    public Result<String> deleteCategory(Long id) {
        GoodsCategory goodsCategory = this.getById(id);
        if(goodsCategory==null||goodsCategory.getDeleted()==1){
            return Result.error("商品不存在");
        }
        boolean b = this.removeById(id);
        if(b) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }


}




