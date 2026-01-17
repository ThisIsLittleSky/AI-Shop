package com.sky.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
public class SysUser {
    private Long id;

    private String username;

    private String password;

    private String phone;

    private String avatar;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}