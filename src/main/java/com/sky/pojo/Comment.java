package com.sky.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment {
    private Long id;

    private Long userId;

    private Long goodsId;

    private Long orderId;

    private String content;

    private Integer score;

    private Date createTime;

    private Integer deleted;
}