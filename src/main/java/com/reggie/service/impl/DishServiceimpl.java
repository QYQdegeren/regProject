package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.dto.DishDto;
import com.reggie.mapper.DishMapper;
import com.reggie.pojo.Dish;

import com.reggie.pojo.DishFlavor;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceimpl extends ServiceImpl<DishMapper, Dish> implements DishService {


    @Autowired
    private DishFlavorService dishFlavorService;


    /**
     * 增加菜品，保存口味数据
     * @param dishDto
     */
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息
        this.save(dishDto);//this指的是DishServiceimpl

        //菜品id
        Long id = dishDto.getId();
        log.info("菜品id"+id);

        List<DishFlavor> flavors = dishDto.getFlavors();

        log.info(flavors.toString());
        log.info("长度"+flavors.size());

        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }
        //保存菜品口味
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品信息和口味
     * @param id
     * @return
     */
    @Override
    public DishDto getByidWithFlover(Long id) {
        //查询菜品基本信息
        Dish dish = this.getById(id);
        //查询口味基本信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);

        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish,dishDto);

        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 修改菜品信息
     * @param dishDto
     */
    @Override
    public void updateDishAndFlover(DishDto dishDto) {
        //获取菜品id
        Long dishId = dishDto.getId();
        //菜品和菜品属性都是根据菜品的id来进行修改
        this.updateById(dishDto);
        //根据dish_id对口味表进行delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishId);
        dishFlavorService.remove(queryWrapper);
        //重新添加数据
        List<DishFlavor> flavors = dishDto.getFlavors();

        for (DishFlavor flavor : flavors) {
            System.out.println(flavor.getDishId());
            log.info("返回的口味数据中是否含有菜的id" + flavor.getDishId());
            flavor.setDishId(dishId);
        }

        //保存菜品信息
        dishFlavorService.saveBatch(flavors);

    }
}
