package com.sky.dto;

import lombok.Data;

/**
 * @Tool:Intellij IDEA
 * @AUthor:周子天
 * @Date:2026-01-18-下午4:13
 * @Version:1.0
 * @Description:商品分页查询
 */
@Data
public class GoodsPageQuery {
    /**
     * 当前页码
     */
    private Integer current = 1;

    /**
     * 每页数量
     */
    private Integer size = 10;

    /**
     * 分类ID（可选）
     */
    private Long categoryId;

    /**
     * 关键词（用于搜索商品名称或描述）
     */
    private String keyword;

    /**
     * 状态（可选，1-上架，0-下架）
     */
    private Integer status;
}
