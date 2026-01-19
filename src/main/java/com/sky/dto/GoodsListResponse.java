// GoodsListResponse.java - 商品列表响应DTO
package com.sky.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
/*
* 商品列表
* */
@Data
public class GoodsListResponse {
    private Long id;
    private Long categoryId;//分类ID
    private String categoryName;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String image;
    private String description;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}