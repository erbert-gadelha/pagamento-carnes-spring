package com.ebgr.pagamento_carnes.configuration;

import com.ebgr.pagamento_carnes.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JwtConfig {

    @Value("${jwt.token.lifetime}")
    private long tokenLifeTime;

    @Value("${jwt.token.secret}")
    private String SECRET;

    /*@Bean
    public JwtUtil jwtUtil () {
        return new JwtUtil(SECRET, tokenLifeTime);
    }*/


}

