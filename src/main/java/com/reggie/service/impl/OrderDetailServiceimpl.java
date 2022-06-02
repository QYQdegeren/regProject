package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.mapper.OrderDetailMapper;
import com.reggie.pojo.OrderDetail;
import com.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceimpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
