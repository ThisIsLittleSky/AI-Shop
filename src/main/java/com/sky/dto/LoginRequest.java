package com.sky.dto;

import lombok.Data;

/**
 * 登录请求DTO
 */
@Data
public class LoginRequest {
    private String username;      // 用户名
    private String password;      // 密码
    private String captchaId;     // 验证码ID
    private String captchaCode;   // 验证码
}

