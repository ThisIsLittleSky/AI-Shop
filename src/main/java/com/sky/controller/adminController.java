package com.sky.controller;

import com.sky.dto.adminLoginRequest;
import com.sky.service.SysAdminService;
import com.sky.service.SysUserService;
import com.sky.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Tool:Intellij IDEA
 * @AUthor:周子天
 * @Date:2026-01-17-下午2:47
 * @Version:1.0
 * @Description:
 */


@RestController
@RequestMapping("/admin123")
public class adminController {


    @Autowired
    SysAdminService sysAdminService;

    /**
     * 生成验证码
     * GET /api/user/captcha
     */
    @GetMapping("/captcha")
    public Result<Map<String, String>> generateCaptcha() {
        return sysAdminService.generateCaptcha();
    }

    /*
    * 管理员登录
    * */
    @PostMapping("/login")
    public Result<String> login(@RequestBody adminLoginRequest request) {
        return sysAdminService.login(request);
    }


}
