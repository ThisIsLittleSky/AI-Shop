package com.sky.dto;

import lombok.Data;

/**
 * 管理员创建分类请求DTO
 */
@Data
public class AdminCategoryCreateRequest {
    private String name;      // 分类名称
    private Long parentId;    // 父分类ID（0表示顶级分类）
    private Integer sort;     // 排序号
    private Integer status;   // 状态（0-禁用，1-启用）
}