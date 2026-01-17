package com.sky.controller;

import com.sky.dto.LoginRequest;
import com.sky.dto.RegisterRequest;
import com.sky.service.SysUserService;
import com.sky.util.CaptchaUtil;
import com.sky.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Tool:Intellij IDEA
 * @AUthor:周子天
 * @Date:2026-01-17-上午10:43
 * @Version:1.0
 * @Description:
 */


@RestController
@RequestMapping("/user")
public class userController {


    @Autowired
    private SysUserService userService;
    @Autowired
    private CaptchaUtil captchaUtil;

    /**
     * 生成验证码
     * GET /api/user/captcha
     */
    @GetMapping("/captcha")
    public Result<Map<String, String>> generateCaptcha() {
        return userService.generateCaptcha();
    }

    /**
     * 用户登录
     * POST /api/user/login
     * Body: {"username": "test", "password": "123456", "captchaId": "xxx", "captchaCode": "ABCD"}
     */
    @PostMapping("/login")
    //读取请求体里的LoginRequest类（已定义），用@RequestBody
    public Result<com.sky.dto.LoginResponse> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    /**
     * 用户注册
     * POST /api/user/register
     * Body: {"username": "test", "password": "123456", "phone": "13800138000", "captchaId": "xxx", "captchaCode": "ABCD"}
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

}
