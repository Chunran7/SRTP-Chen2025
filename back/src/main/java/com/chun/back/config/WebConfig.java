package com.chun.back.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.chun.back.interceptors.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 启用登录拦截器，排除公开接口（登录/注册/查询接口/静态资源）
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/admin/login",
                        "/article/list",
                        "/article/latest",
                        "/article/{id}",
                        "/video/**",
                        "/post/list",
                        "/post/*",
                        "/error",
                        "/static/**",
                        "/favicon.ico");
    }
}