package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.pojo.Comment;
import com.sky.service.CommentService;
import com.sky.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author SKY
* @description 针对表【comment(商品评论表)】的数据库操作Service实现
* @createDate 2026-01-17 09:34:53
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




