package com.sky.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @TableName order_item
 */
@TableName(value ="order_item")
@Data
public class OrderItem {
    private Long id;

    private Long orderId;

    private Long goodsId;

    private String goodsName;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal totalPrice;
}