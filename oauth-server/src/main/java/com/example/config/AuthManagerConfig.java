package com.example.config;

import com.example.manager.BaseAuthManager;
import com.example.manager.TokenAuthManager;
import com.example.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthManagerConfig {
    @Bean
    public BaseAuthManager sessionAuthManager( @Autowired TokenService tokenService) {
        return new TokenAuthManager(tokenService);
    }
}
