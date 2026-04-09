package com.chun.back.config;

// ...existing code... (removed unused Autowired)
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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
                                                "http://localhost:5173", // 本地开发环保（可选）
                                                "http://127.0.0.1:5173" // 本地开发环保（可选）
                                )
                                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                                .allowedHeaders("*")
                                .allowCredentials(true)
                                .maxAge(3600);
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                // 目前不启用全局登录拦截器（用户端登录已不强制），
                // 如果未来需要恢复，请在这里注册相应拦截器
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
