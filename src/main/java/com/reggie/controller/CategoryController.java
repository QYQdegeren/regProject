package com.reggie.controller;


import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.pojo.Category;
import com.reggie.pojo.Employee;
import com.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /*
    新增分类
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category"+ category.toString());
        categoryService.save(category);
        return R.success("添加分类成功成功");
    }


    /**
     * 分页查询分类
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){

        //构造分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }


    /**
     * 根据id删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){

//        categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("消息删除成功");
    }

    /**
     * 修改消息，
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("信息修改成功");

    }


    /**
     * 根据条件查询分类数据
     * @param
     * @return
     */
    @GetMapping("/list")
    public R< List<Category> > list(Category category){

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        queryWrapper.eq(category.getType()!=null, Category::getType,category.getType());

        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
