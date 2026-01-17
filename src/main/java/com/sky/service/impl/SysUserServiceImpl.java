package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.pojo.SysUser;
import com.sky.service.SysUserService;
import com.sky.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
* @author SKY
* @description 针对表【sys_user(普通用户表)】的数据库操作Service实现
* @createDate 2026-01-17 09:34:53
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

}




