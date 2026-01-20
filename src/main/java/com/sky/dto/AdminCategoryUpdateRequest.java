package com.sky.dto;

import lombok.Data;

/**
 * 管理员更新分类请求DTO
 */
@Data
public class AdminCategoryUpdateRequest {
    private String name;      // 分类名称
    private Long parentId;    // 父分类ID
    private Integer sort;     // 排序号
    private Integer status;   // 状态（0-禁用，1-启用）
}