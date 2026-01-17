package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.pojo.Goods;
import com.sky.service.GoodsService;
import com.sky.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

/**
* @author SKY
* @description 针对表【goods(商品表)】的数据库操作Service实现
* @createDate 2026-01-17 09:34:53
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

}




