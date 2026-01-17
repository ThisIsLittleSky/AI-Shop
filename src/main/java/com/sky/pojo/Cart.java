package com.sky.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName cart
 */
@TableName(value ="cart")
@Data
public class Cart {
    private Long id;

    private Long userId;

    private Long goodsId;

    private Integer quantity;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}