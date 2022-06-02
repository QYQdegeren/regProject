package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.dto.DishDto;
import com.reggie.pojo.Category;
import com.reggie.pojo.Dish;
import com.reggie.pojo.DishFlavor;
import com.reggie.service.CategoryService;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.LambdaConversionException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        log.info(dishDto.toString());
//        System.out.println(dishDto.toString());
//        System.out.println(dishDto.getImage());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }


    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page , int pageSize , String name){
        Page<Dish> pageInfo = new Page<>(page , pageSize);

        Page<DishDto> dishDtoPage = new Page<>(page , pageSize);


        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null , Dish::getName , name);//模糊查询
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝 pageInfo 拷贝到 dishDtoPage
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<DishDto> dtos = new ArrayList<>();
        List<Dish> records = pageInfo.getRecords();
        for (Dish record : records) {

            DishDto dishDto = new DishDto();
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);

            BeanUtils.copyProperties(record,dishDto);
            dishDto.setCategoryName(category.getName());
            dtos.add(dishDto);
        }

        dishDtoPage.setRecords(dtos);

        return R.success(dishDtoPage);
    }


    /**
     * 根据id查询菜品信息,回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> up(@PathVariable Long id){
        log.info("传回菜品id是"+id);

        DishDto dishDto = dishService.getByidWithFlover(id);
        return R.success(dishDto);
    }


    /**
     * 修改菜品信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
//        System.out.println(dishDto.toString());
        dishService.updateDishAndFlover(dishDto);
        return R.success("修改成功");
    }


    /**
     * 修改菜品的状态，获取url地址中的数据，形参必须放在前面
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(Long ids , @PathVariable Integer status){

        log.info("状态u---"+status,ids);
        System.out.println(ids);
        //根据id查出dish
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getId,ids);
        Dish one = dishService.getOne(queryWrapper);
        one.setStatus(status);
        dishService.updateById(one);
        return R.success("修改成功");
    }

    /**
     * 根据分类的id查询响应的菜品
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        //查询状态为1 的菜品
//        queryWrapper.eq(Dish::getStatus,1);
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //查询状态为1 的菜品
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> listDto = new ArrayList<>();
        for (Dish dish1 : list) {

            DishDto dishDto = new DishDto();
            Long categoryId = dish1.getCategoryId();
            Category category = categoryService.getById(categoryId);

            BeanUtils.copyProperties(dish1,dishDto);
            dishDto.setCategoryName(category.getName());

            //当前菜品的id
            Long id = dish1.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper();
            queryWrapper1.eq(DishFlavor::getDishId,id);
            List<DishFlavor> flavorList = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(flavorList);

            listDto.add(dishDto);
        }

        return R.success(listDto);
    }
}
