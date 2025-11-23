package com.microservices.viajes.clients;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.http.HttpHeaders;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // toma la request que le llega al controller
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // toma el header para buscar el token
                String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

                // 3. si tiene token lo pega
                if (authorizationHeader != null) {
                    requestTemplate.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
                }
            }
        };
    }
}