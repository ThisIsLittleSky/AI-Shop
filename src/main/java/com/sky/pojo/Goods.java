package com.sky.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName goods
 */
@TableName(value ="goods")
@Data
public class Goods {
    private Long id;

    private Long categoryId;

    private String name;

    private BigDecimal price;

    private Integer stock;

    private String image;

    private String description;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}