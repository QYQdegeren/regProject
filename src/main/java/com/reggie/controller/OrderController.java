package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.BaseContext;
import com.reggie.common.R;
import com.reggie.pojo.Orders;
import com.reggie.service.OrdersService;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){

        ordersService.submit(orders);
        return R.success("下单成功");
    }


    @GetMapping("/userPage")
    public R<Page> userpage(int page,int pageSize){

        log.info("page {} {}" , page,pageSize);
        //获取当前用户id
        Long userid = BaseContext.getCurrentId();

        Page<Orders> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,userid);

        ordersService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){


        //获取当前用户id
        Long userid = BaseContext.getCurrentId();

        Page<Orders> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,userid);

        ordersService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

}
