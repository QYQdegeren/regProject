package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.SetmealDto;
import com.reggie.pojo.Setmeal;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {

    @Transactional//需要操作两张表，加入事务注解
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐和关联数据
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids);
}
