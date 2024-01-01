package com.wzy.config;

import com.wzy.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())//拦截接口请求
                .addPathPatterns("/**").excludePathPatterns("/user/login","/user/register","/file/*");    //,"/**/export","/**/import" 拦截所有请求，判断token是否合法来觉得是否需要登录
    }

    @Bean//必须注入一下，不知道为什么
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }
}
