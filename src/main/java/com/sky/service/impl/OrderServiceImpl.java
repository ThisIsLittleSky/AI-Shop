package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.pojo.Order;
import com.sky.service.OrderService;
import com.sky.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author SKY
* @description 针对表【order(订单表)】的数据库操作Service实现
* @createDate 2026-01-17 09:34:53
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




