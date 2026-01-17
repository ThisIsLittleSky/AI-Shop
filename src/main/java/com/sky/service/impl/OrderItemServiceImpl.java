package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.pojo.OrderItem;
import com.sky.service.OrderItemService;
import com.sky.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

/**
* @author SKY
* @description 针对表【order_item(订单明细表)】的数据库操作Service实现
* @createDate 2026-01-17 09:34:53
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService{

}




