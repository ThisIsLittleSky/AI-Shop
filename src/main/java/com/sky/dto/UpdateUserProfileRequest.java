package com.sky.dto;

import lombok.Data;

/**
 * 更新个人资料请求DTO
 */
@Data
public class UpdateUserProfileRequest {
    private String phone;      // 手机号
    private String avatar;     // 头像URL
}