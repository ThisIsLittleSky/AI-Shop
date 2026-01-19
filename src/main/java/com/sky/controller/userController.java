package com.sky.controller;

import com.sky.dto.LoginRequest;
import com.sky.dto.RegisterRequest;
import com.sky.dto.UpdateUserProfileRequest;
import com.sky.dto.UserProfileResponse;
import com.sky.service.SysUserService;
import com.sky.util.CaptchaUtil;
import com.sky.util.OssUtil;
import com.sky.util.Result;
import com.sky.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private OssUtil ossUtil;

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

    /**
     * 查询个人资料
     * GET /api/user/profile
     * Headers: Authorization: Bearer {token}
     */
    // TODO 加入用户昵称，现有的username当做账号
    @GetMapping("/profile")
    public Result<UserProfileResponse> getProfile(HttpServletRequest request) {
        Long userId = UserContext.getUserId(request);
        if (userId == null) {
            return Result.error(400,"未登录或token无效");
        }
        return userService.getProfile(userId);
    }

    /**
     * 更新个人资料
     * PUT /api/user/profile
     * Headers: Authorization: Bearer {token}
     * Body: {"phone": "13800138000", "avatar": "http://example.com/avatar.jpg"}
     */
    @PutMapping("/profile")
    public Result<String> updateProfile(
            HttpServletRequest HttpRequest,
            @RequestBody UpdateUserProfileRequest request
    ){
        Long userId = UserContext.getUserId(HttpRequest);
        if (userId == null) {
            return Result.error(404,"未登录或token无效");
        }
        return userService.updateProfile(userId, request);
    }

    /**
     * 上传头像
     * POST /api/user/upload/avatar
     * Headers: Authorization: Bearer {token}
     * Content-Type: multipart/form-data
     * Body: file (文件)
     */
    @PostMapping("/upload/avatar")
    public Result<String> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        try {
            Long userId = UserContext.getUserId(request);
            if (userId == null) {
                return Result.error(401, "未登录或token无效");
            }
            
            // 上传到OSS
            String fileUrl = ossUtil.uploadAvatar(file);
            
            return Result.success(fileUrl);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "上传失败：" + e.getMessage());
        }
    }
}
