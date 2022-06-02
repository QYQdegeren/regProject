package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.DishDto;
import com.reggie.pojo.Dish;
import com.reggie.pojo.DishFlavor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DishService extends IService<Dish> {

    //新增加菜品,操作两张表

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByidWithFlover(Long id);

    public void updateDishAndFlover(DishDto dishDto);
}
