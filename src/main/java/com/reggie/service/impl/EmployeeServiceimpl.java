package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.mapper.EmployeeMapper;
import com.reggie.pojo.Employee;
import com.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

//MybatisPlus 提供的ServiceImpl类
@Service
public class EmployeeServiceimpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
