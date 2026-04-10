package com.chun.back.config;

// ...existing code... (removed unused Autowired)
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import com.chun.back.interceptors.LoginInterceptor;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Autowired
        private LoginInterceptor loginInterceptor;

        @Value("${file.upload-path}")
        private String uploadPath;

        @Value("${file.sub-paths.article:articles/}")
        private String articleSubPath;

        @Value("${file.sub-paths.content:content/}")
        private String contentSubPath;

        @Value("${file.sub-paths.introduction:introductions/}")
        private String introductionSubPath;

        @Value("${file.sub-paths.avatar:avatars/}")
        private String avatarSubPath;

        @Override
        public void addCorsMappings(CorsRegistry registry) {
                // 全局跨域配置 - 宝塔面板部署
                registry.addMapping("/**")
                                .allowedOrigins(
                                                "http://1.116.117.203", // 服务器IP
                                                "https://1.116.117.203", // HTTPS版本
                                                // 本地开发环境（可选）
                                                "http://localhost:5173",
                                                "http://127.0.0.1:5173",
                                                // 生产域名：允许前端在生产域下发起请求（HTTP/HTTPS）
                                                "https://www.yilutongxing-zyst.com",
                                                "http://www.yilutongxing-zyst.com",
                                                "https://yilutongxing-zyst.com",
                                                "http://yilutongxing-zyst.com")
                                // 也兼容子域名匹配（若需要更灵活的模式匹配，取消注释下一行并视情况使用）
                                // .allowedOriginPatterns("https://*.yilutongxing-zyst.com",
                                // "http://*.yilutongxing-zyst.com")
                                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                                .allowedHeaders("*")
                                .allowCredentials(true)
                                .maxAge(3600);
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                // 注册登录拦截器：保护管理员与写操作接口
                registry.addInterceptor(loginInterceptor)
                                .addPathPatterns("/admin/**", "/article/**")
                                // 登录接口放行
                                .excludePathPatterns("/admin/login")
                                // 静态资源与跨域预检放行
                                .excludePathPatterns("/images/**", "/favicon.ico", "/**/*.css", "/**/*.js");
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // 配置静态资源映射：当访问 /images/** 时，从本地磁盘路径读取
                // 映射所有子目录，支持分类图片访问
                registry.addResourceHandler("/images/**")
                                .addResourceLocations(
                                                "file:" + uploadPath.replace("\\", "/"),
                                                "file:" + (uploadPath + articleSubPath).replace("\\", "/"),
                                                "file:" + (uploadPath + contentSubPath).replace("\\", "/"),
                                                "file:" + (uploadPath + introductionSubPath).replace("\\", "/"),
                                                "file:" + (uploadPath + avatarSubPath).replace("\\", "/"));
        }
}
