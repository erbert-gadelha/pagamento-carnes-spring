package com.ebgr.pagamento_carnes.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig /*implements WebMvcConfigurer*/ {
    /*     @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Substitua por domínios específicos em produção
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }*/
}
