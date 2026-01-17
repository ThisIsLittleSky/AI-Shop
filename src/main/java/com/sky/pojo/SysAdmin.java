package com.sky.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName sys_admin
 */
@TableName(value ="sys_admin")
@Data
public class SysAdmin {
    private Long id;

    private String username;

    private String password;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}