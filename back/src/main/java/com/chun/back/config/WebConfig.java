package com.chun.back.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.chun.back.interceptors.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Value("${file.upload-path}")
    private String uploadPath;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 全局跨域配置
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

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
                        "/favicon.ico",
                        "/images/**",
                        "/api/upload"); // 添加：排除文件上传接口
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射：当访问 /images/** 时，从本地磁盘路径读取
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadPath.replace("\\", "/"));
    }
}
