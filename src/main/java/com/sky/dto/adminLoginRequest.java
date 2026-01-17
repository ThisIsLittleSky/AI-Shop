package com.sky.dto;

import lombok.Data;

/**
 * @Tool:Intellij IDEA
 * @AUthor:周子天
 * @Date:2026-01-17-下午2:51
 * @Version:1.0
 * @Description:
 */
@Data
public class adminLoginRequest {

    private String username;
    private String password;
    private String captchaId;     // 验证码ID
    private String captchaCode;   // 验证码
}
