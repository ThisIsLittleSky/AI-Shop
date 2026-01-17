package com.sky.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName delivery_info
 */
@TableName(value ="delivery_info")
@Data
public class DeliveryInfo {
    private Long id;

    private Long orderId;

    private String expressCompany;

    private String expressNo;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}