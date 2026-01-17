package com.sky.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName rag_knowledge
 */
@TableName(value ="rag_knowledge")
@Data
public class RagKnowledge {
    private Long id;

    private String title;

    private String content;

    private String filePath;

    private String vectorId;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}