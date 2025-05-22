package com.kroum.kroum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 API 경로에 대해
                .allowedOrigins("http://localhost:3000") // 프론트 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 메서드
                .allowCredentials(true); // 세션/쿠키 전달 허용
    }
}