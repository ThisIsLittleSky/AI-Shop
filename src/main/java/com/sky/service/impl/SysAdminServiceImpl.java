package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.adminLoginRequest;
import com.sky.pojo.Goods;
import com.sky.pojo.SysAdmin;
import com.sky.service.SysAdminService;
import com.sky.mapper.SysAdminMapper;
import com.sky.util.CaptchaUtil;
import com.sky.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author SKY
* @description 针对表【sys_admin(管理员表)】的数据库操作Service实现
* @createDate 2026-01-17 09:34:53
*/
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService{

    @Autowired
    private CaptchaUtil captchaUtil;

    /*
    * 验证码实现
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
    * 管理员登录
    * */
    @Override
    public Result<String> login(adminLoginRequest request) {
        // 1. 验证验证码(CaptchaId是校验验证，用于核对验证码。CaptchaCode是用户输入)
        boolean captchaValid = captchaUtil.verifyCaptcha(
                request.getCaptchaId(),
                request.getCaptchaCode()
        );
        if (!captchaValid) {
            return Result.error("验证码过期/错误");
        }
        if(request.getUsername()==null && request.getPassword()==null){
            return Result.error(400,"用户名或密码不能为空");
        }
        LambdaQueryWrapper<SysAdmin> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SysAdmin::getUsername,request.getUsername());
        queryWrapper.eq(SysAdmin::getDeleted,0);
        SysAdmin sysAdmin = this.getOne(queryWrapper);
        if(sysAdmin==null){
            return Result.error(400,"用户或密码错误");
        }
        if(sysAdmin.getStatus()!=null&&sysAdmin.getStatus()==0){
            return Result.error(400,"用户被禁用");
        }
        // 5. 验证密码（使用MD5加密，你可以根据需要改成其他加密方式）
        String encryptedPassword = DigestUtils.md5DigestAsHex(
                request.getPassword().getBytes()
        );
        if (!encryptedPassword.equals(sysAdmin.getPassword())) {
            return Result.error(400, "用户名或密码错误");
        }
        // 6. 登录成功（这里可以生成JWT token或session）
        return Result.success("登录成功");
    }



}




