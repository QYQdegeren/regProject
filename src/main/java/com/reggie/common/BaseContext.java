package com.reggie.common;


/**
 * 基于ThreadLocal封装的工具类
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    //static方法，直接通过类名调用方法
    public static void  setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){

        return threadLocal.get();
    }
}
