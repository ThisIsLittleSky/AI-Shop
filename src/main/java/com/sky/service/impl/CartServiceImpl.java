package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.pojo.Cart;
import com.sky.service.CartService;
import com.sky.mapper.CartMapper;
import org.springframework.stereotype.Service;

/**
* @author SKY
* @description 针对表【cart(购物车表)】的数据库操作Service实现
* @createDate 2026-01-17 09:34:53
*/
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart>
    implements CartService{

}




