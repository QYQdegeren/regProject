package com.reggie.filter;


import com.alibaba.fastjson.JSON;
import com.reggie.common.BaseContext;
import com.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 检查用户是否已经登陆
 * filter , 过滤器，web提供的功能，拦截器，MVC提供的功能
 */

@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {


    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    //

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        //获取本次请求的URI
        String uri = request.getRequestURI();
        log.info("拦截到请求 {}",request.getRequestURI());
        //定义不需要请求的的路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
//                "/favicon.ico",
//                "/employee/page"
        };
        //判断本次请求是否需要处理,将uri与urls中的字符串进行比较
        boolean check = check(urls, uri);
        if(check){
            log.info("本次请求{}不需要处理",uri);
            //check == true ,请求不需要处理，直接放行
            filterChain.doFilter(request,response);
            return;
        }


        HttpSession session = request.getSession();



        if(session.getAttribute("employee")!=null){
            //能在session中取出数据，怎么已经登陆，直接放行
            log.info("用户已登陆，用户id {}",session.getAttribute("employee"));

            //将目前用户id存入ThreadLocal
            BaseContext.setCurrentId((Long) session.getAttribute("employee"));

            filterChain.doFilter(request,response);
            return;
        }

//        //判断移动端用户的登陆状态
        if(session.getAttribute("user")!=null){
            //能在session中取出数据，怎么已经登陆，直接放行
            log.info("用户已登陆，用户id {}",session.getAttribute("user"));

            //将目前用户id存入ThreadLocal
            BaseContext.setCurrentId((Long) session.getAttribute("user"));

            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户没有登陆");
        //用户没有登陆，通过输出流的方式，向客户端响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param uri
     * @param urls
     * @return
     */
    public boolean check(String[] urls,String uri){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, uri);
            if(match){
                return true;
            }
        }
        return false;
    }
}
