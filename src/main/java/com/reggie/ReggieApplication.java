package com.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan//扫描WebFilter注解，添加过滤器
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动");
        System.out.println("hello got");
        System.out.println("git 333333");
        System.out.println("远程的修改");
        System.out.println("本地的修改");
        System.out.println("本地是修改====");
        System.out.println("本地的修改=====2222");
    }
}
