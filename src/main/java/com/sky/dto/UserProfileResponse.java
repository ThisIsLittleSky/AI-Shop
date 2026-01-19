package com.sky.dto;

import lombok.Data;
import java.util.Date;

/**
 * 个人资料响应DTO
 * 用于返回用户的基本信息（不包含密码等敏感信息）
 */
@Data
public class UserProfileResponse {
    private Long id;
    private String username;
    private String phone;
    private String avatar;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}