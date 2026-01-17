package com.sky.dto;

import lombok.Data; /**
 * 注册请求DTO
 */
@Data
public class RegisterRequest {
    private String username;      // 用户名
    private String password;      // 密码
    private String phone;         // 手机号（可选）
    private String captchaId;     // 验证码ID
    private String captchaCode;   // 验证码
}
