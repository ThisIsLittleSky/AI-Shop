package com.sky.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 管理员更新商品请求DTO
 */
@Data
public class AdminGoodsUpdateRequest {
    private Long categoryId;      // 分类ID
    private String name;          // 商品名称
    private BigDecimal price;     // 商品价格
    private Integer stock;        // 库存数量
    private String image;         // 商品主图URL
    private String description;   // 商品描述
    private Integer status;       // 状态（0-下架，1-上架）
}