package com.sky.service;

import com.sky.dto.*;
import com.sky.pojo.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.util.Result;

import java.util.Map;

/**
* @author SKY
* @description 针对表【sys_user(普通用户表)】的数据库操作Service
* @createDate 2026-01-17 09:34:53
*/
public interface SysUserService extends IService<SysUser> {

    //生成验证码
    Result<Map<String, String>> generateCaptcha();
    //用户登录
    Result<LoginResponse> login(LoginRequest request);
    //用户注册
    Result<String> register(RegisterRequest request);
    //用户信息查询
    Result<UserProfileResponse> getProfile(Long userId);
    //修改用户信息
    Result<String> updateProfile(Long userId, UpdateUserProfileRequest request);
}
