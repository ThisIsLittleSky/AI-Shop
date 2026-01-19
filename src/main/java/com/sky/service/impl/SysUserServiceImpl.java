package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.*;
import com.sky.pojo.SysUser;
import com.sky.service.SysUserService;
import com.sky.mapper.SysUserMapper;
import com.sky.util.CaptchaUtil;
import com.sky.util.JwtUtil;
import com.sky.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
* @author SKY
* @description 针对表【sys_user(普通用户表)】的数据库操作Service实现
* @createDate 2026-01-17 09:34:53
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{
    @Autowired
    private CaptchaUtil captchaUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;


    /*
    * 生成验证码服务
    * */
    @Override
    public Result<Map<String, String>> generateCaptcha() {
        CaptchaUtil.CaptchaResult result = captchaUtil.generateCaptcha();

        Map<String, String> data = new HashMap<>();
        data.put("captchaId", result.getCaptchaId());
        data.put("base64Image", result.getBase64Image());

        return Result.success(data);
    }
    /*
    * 用户登录实现
    * */
    @Override
    public Result<LoginResponse> login(LoginRequest request) {
        // 1. 验证验证码(CaptchaId是校验验证，用于核对验证码。CaptchaCode是用户输入)
        boolean captchaValid = captchaUtil.verifyCaptcha(
                request.getCaptchaId(),
                request.getCaptchaCode()
        );

        if (!captchaValid) {
            return Result.error(400, "验证码错误或已过期");
        }

        // 2. 验证用户名和密码（.trim去除获取到的原始密码字符串「首尾两端」的所有空白字符）
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return Result.error(400, "用户名不能为空");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return Result.error(400, "密码不能为空");
        }
        // 3. 查询用户
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, request.getUsername().trim());
        queryWrapper.eq(SysUser::getDeleted, 0);

        SysUser user = this.getOne(queryWrapper);

        if (user == null) {
            return Result.error(400, "用户名或密码错误");
        }
// 4. 验证用户状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            return Result.error(400, "账户已被禁用");
        }

        // 5. 验证密码（使用MD5加密，你可以根据需要改成其他加密方式）
        String encryptedPassword = DigestUtils.md5DigestAsHex(
                request.getPassword().getBytes()
        );

        if (!encryptedPassword.equals(user.getPassword())) {
            return Result.error(400, "用户名或密码错误");
        }

        // 6. 登录成功，生成JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), "user");
        
        // 7. 构建登录响应
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setUserId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setUserType("user");
        loginResponse.setExpiration(System.currentTimeMillis() + expiration);

        return Result.success(loginResponse);

    }

    @Override
    public Result<String> register(RegisterRequest request) {
        // 1. 验证验证码
        boolean captchaValid = captchaUtil.verifyCaptcha(
                request.getCaptchaId(),
                request.getCaptchaCode()
        );

        if (!captchaValid) {
            return Result.error(400, "验证码错误或已过期");
        }

        // 2. 验证输入参数
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return Result.error(400, "用户名不能为空");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return Result.error(400, "密码不能为空");
        }

        if (request.getPassword().length() < 6) {
            return Result.error(400, "密码长度不能少于6位");
        }
        // 3. 检查用户名是否已存在
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, request.getUsername().trim());
        queryWrapper.eq(SysUser::getDeleted, 0);

        long count = this.count(queryWrapper);
        if (count > 0) {
            return Result.error(400, "用户名已存在");
        }

        // 4. 创建新用户
        SysUser newUser = new SysUser();
        newUser.setUsername(request.getUsername().trim());
        // 密码加密（使用MD5，建议使用BCrypt或其他更安全的加密方式）
        String encryptedPassword = DigestUtils.md5DigestAsHex(
                request.getPassword().getBytes()
        );
        newUser.setPassword(encryptedPassword);
        newUser.setPhone(request.getPhone());
        newUser.setStatus(1); // 默认启用
        newUser.setDeleted(0); // 未删除

        // 5. 保存用户
        boolean saved = this.save(newUser);
        if (saved) {
            return Result.success("注册成功");
        } else {
            return Result.error(500, "注册失败，请稍后重试");
        }
    }

    //用户信息查询
    @Override
    public Result<UserProfileResponse> getProfile(Long userId) {
        //1.用lambadaQueryWrapper构建查询条件。.eq(SysUser::getId,userId)
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getId, userId);
        //2.查询
        SysUser user = this.getOne(queryWrapper);
        //3.判断用户是否存在、是否被禁用
        if (user == null) {
            return Result.error(404,"用户不存在");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            return Result.error(400,"用户已被禁用");
        }
        //4.新建DTO类并用BeanUtils工具把user属性copy给userProfileResponse。
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        BeanUtils.copyProperties(user, userProfileResponse);
        return Result.success(userProfileResponse);
    }

    @Override
    public Result<String> updateProfile(Long userId, UpdateUserProfileRequest request) {
        //1.构建查询条件
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getId,userId);
        //2.查询后判断用户状态
        SysUser user = this.getOne(queryWrapper);
        if (user == null) {
            return Result.error(404,"用户不存在");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            return Result.error(404,"用户被禁用");
        }
        // 3. 更新可修改的字段
        boolean needUpdate = false;
        //4.验证手机号
        //trim() 是 Java String类的内置实例方法，其核心功能是去除字符串的开头（前导）和结尾（尾随）的空白字符
        if(request.getPhone()==null && request.getPhone().trim().length()!=11){
            return Result.error(400, "手机号格式不正确");
        }
        user.setPhone(request.getPhone().trim());
        needUpdate = true;
        // 5.更新头像
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar().trim().isEmpty() ? null : request.getAvatar().trim());
            needUpdate = true;
        }
        if (needUpdate) {
            this.updateById(user);
            return Result.success("更新成功");
        }else{
            return Result.error(400,"没有需要更新的字段");
        }
    }
}




