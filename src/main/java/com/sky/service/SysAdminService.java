package com.sky.service;

import com.sky.dto.adminLoginRequest;
import com.sky.pojo.SysAdmin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.util.Result;

import java.util.Map;

/**
* @author SKY
* @description 针对表【sys_admin(管理员表)】的数据库操作Service
* @createDate 2026-01-17 09:34:53
*/
public interface SysAdminService extends IService<SysAdmin> {
    //验证码
    Result<Map<String, String>> generateCaptcha();
    //管理员登录
    Result<String> login(adminLoginRequest request);

}
