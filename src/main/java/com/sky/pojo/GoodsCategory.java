package com.sky.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName goods_category
 */
@TableName(value ="goods_category")
@Data
public class GoodsCategory {
    private Long id;

    private String name;

    private Long parentId;

    private Integer sort;

    private Integer status;

    private Integer deleted;
}