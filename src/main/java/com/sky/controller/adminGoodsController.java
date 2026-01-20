package com.sky.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sky.annotation.RequireAdmin;
import com.sky.dto.*;
import com.sky.pojo.Goods;
import com.sky.pojo.GoodsCategory;
import com.sky.service.GoodsCategoryService;
import com.sky.service.GoodsService;
import com.sky.util.OssUtil;
import com.sky.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Tool:Intellij IDEA
 * @AUthor:周子天
 * @Date:2026-01-20-下午12:18
 * @Version:1.0
 * @Description:商品管理
 */

@RestController
@RequestMapping("/admin123/goods")
@RequireAdmin
public class adminGoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OssUtil ossUtil;

    @Autowired
    private GoodsCategoryService goodsCategoryService;


    /**
     * 管理员获取商品列表（支持筛选/检索）
     * GET /admin123/goods/list
     */
    @GetMapping("/list")
    public Result<IPage<GoodsListResponse>> list(GoodsPageQuery goodsPageQuery) {
        //1.设置默认值
        if (goodsPageQuery.getCurrent() == null) {
            goodsPageQuery.setCurrent(1);
        }
        if (goodsPageQuery.getSize() == null) {
            goodsPageQuery.setSize(10);
        }
        return goodsService.getAdminGoodsList(goodsPageQuery);
    }

    /**
     * 管理员获取商品详情
     * GET /admin123/goods/detail/{id}
     */
    @GetMapping("/detail/{id}")
    public Result<GoodsListResponse> detail(@PathVariable Long id) {
        if (id == null) {
            return Result.error(404,"商品ID为输入");
        }
        return goodsService.getGoodsDetail(id);
    }

    /**
     * 管理员创建商品
     * POST /admin123/goods/create
     */
    @PostMapping("/create")
    public Result<String> createGoods(@RequestBody AdminGoodsCreateRequest request) {
        return goodsService.createGoods(request);
    }

    /**
     * 管理员更新商品
     * PUT /admin123/goods/update/{id}
     */
    @PutMapping("/update/{id}")
    public Result<String> updateGoods(
            @PathVariable Long id,
            @RequestBody AdminGoodsUpdateRequest request) {
        return goodsService.updateGoods(id, request);
    }

    /**
     * 管理员删除商品（逻辑删除）
     * DELETE /admin123/goods/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteGoods(@PathVariable Long id) {
        return goodsService.deleteGoods(id);
    }

    /**
     * 管理员批量删除商品
     * POST /admin123/goods/batch-delete
     */
    @PostMapping("/batch-delete")
    public Result<String> batchDeleteGoods(@RequestBody List<Long> ids) {
        return goodsService.batchDeleteGoods(ids);
    }

    /**
     * 管理员上传商品图片
     * POST /admin123/goods/upload/image
     */
    @PostMapping("/upload/image")
    public Result<String> uploadGoodsImage(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        try {
            // 上传到OSS的AI_Shop_Goods文件夹
            String fileUrl = ossUtil.uploadFile(file, "AI_Shop_Goods");
            return Result.success(fileUrl);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "上传失败：" + e.getMessage());
        }
    }

    /**
     * 管理员获取所有商品分类（包括禁用的）
     * GET /admin123/goods/categories
     */
    @GetMapping("/categories")
    public Result<List<GoodsCategory>> getAllCategories() {
        return goodsCategoryService.getAllCategories();
    }

    /**
     * 管理员创建商品分类
     * POST /admin123/goods/category/create
     */
    @PostMapping("/category/create")
    public Result<String> createCategory(@RequestBody AdminCategoryCreateRequest request) {
        return goodsCategoryService.createCategory(request);
    }
    /**
     * 管理员更新商品分类
     * PUT /admin123/goods/category/update/{id}
     * 修改都要接收一个Id
     */
    @PutMapping("/category/update/{id}")
    public Result<String> updateCategory(
            @RequestBody AdminCategoryUpdateRequest request,
            @PathVariable Long id) {
        return goodsCategoryService.updateCategory(id, request);
    }


    /**
     * 管理员删除商品分类（逻辑删除）
     * DELETE /admin123/goods/category/delete/{id}
     */
    @DeleteMapping("/category/delete/{id}")
    public Result<String> deleteCategory(@PathVariable Long id) {
        return goodsCategoryService.deleteCategory(id);
    }
}
