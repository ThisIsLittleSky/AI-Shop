package com.sky.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 配置JWT拦截器和排除路径
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // JWT拦截器（先执行）
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        // 用户相关接口（不需要登录）
                        "/user/captcha",
                        "/user/login",
                        "/user/register",
                        // 管理员相关接口（不需要登录）
                        "/admin123/captcha",
                        "/admin123/login",
                        // 静态资源
                        "/**/*.html",
                        "/**/*.js",
                        "/**/*.css",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.gif",
                        "/**/*.ico",
                        // Swagger相关（如果有）
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**"
                );

        // 管理员权限拦截器（后执行，依赖于JWT拦截器设置的用户信息）
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/**");
    }
}

