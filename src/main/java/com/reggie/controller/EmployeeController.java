package com.reggie.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.pojo.Employee;
import com.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登陆
     * 除了传如页面的数据，还需要一个HttpServletRequest的对象，用于将emploee的id存入Session
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request , @RequestBody Employee employee){

        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee one = employeeService.getOne(queryWrapper);

        //3、如果没有查询到则返回登陆失败的结果
        if(one == null){
            return R.error("登陆失败，用户不存在");
        }

        //4、密码比对，如果不一样则返回失败结果
        if(!one.getPassword().equals(password)){
            return R.error("登陆失败，密码错误");
        }

        //5、查看员工状态
        if(one.getStatus()==0){
            //员工处于禁用状态
            return R.error("登陆失败，账号已经禁用");
        }

        //6、登陆成功，将员工的id存入Session
        request.getSession().setAttribute("employee",one.getId());
        return R.success(one);

    }


    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中的员工id
        HttpSession session = request.getSession();
        session.removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request , @RequestBody Employee employee){
        //添加新员工，设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //设置创建和修改的时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //添加创建、修改人的id
//        long emid = (long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(emid);
//        employee.setUpdateUser(emid);

        //调用service方法，保存数据
        employeeService.save(employee);

        return R.success("新增成功!");
    }

    /**
     * 员工消息的分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加一个过滤条件
        queryWrapper.eq(!StringUtils.isEmpty(name),Employee::getName,name);
        //添加一个排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询,MP封装的分页查询功能
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request , @RequestBody Employee employee){
//        log.info((employee.toString()));
//        long empid = (long) request.getSession().getAttribute("employee");

//        employee.setUpdateUser(empid);

//        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);//根据id修改数据
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工消息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<Employee> getById(@PathVariable Long id){
//        log.info(id.toString());
        Employee emp = employeeService.getById(id);

        return R.success(emp);
    }

}
