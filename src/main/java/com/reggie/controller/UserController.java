package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggie.common.R;
import com.reggie.pojo.User;
import com.reggie.service.UserService;
import com.reggie.utils.SMSUtils;
import com.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            //随机生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            log.info("code = {}",code);//通过控制台查看验证码
//            SMSUtils.sendMessage("瑞吉外卖","",phone,code);
//            session.setAttribute(phone,code);

            //将生成的验证码缓存到redis中
            ValueOperations valueOperations = redisTemplate.opsForValue();
            valueOperations.set(phone,code,5, TimeUnit.MINUTES);

            return R.success("验证码发送成功");
        }
        return R.error("发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map , HttpSession session){



        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();

        //从redis中获取验证码
        String codeInSession = (String)redisTemplate.opsForValue().get(phone);

        //从Session中得到保存的验证码
//        Object codeInSession = session.getAttribute(phone);

        //进行验证码比对
        if(codeInSession!=null && codeInSession.equals(code)){
            //如果是新用户，注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user == null){
                //创建新用户
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());

            //用户登陆成功，输出redis中缓存的验证码
            redisTemplate.delete(phone);

            return R.success(user);

        }

        return R.error("登陆失败");
    }
}
