package com.mju.board.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST = {
            "/**"
    };

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authorizeHttpRequests(authorize -> {
            try {
                authorize
                        .requestMatchers("/board-service/enrollment").hasRole("ADMIN")
                        .anyRequest().permitAll()
                        .and()
                        .formLogin().loginPage("http://localhost:8000/auth-service/login")
                        .loginProcessingUrl("/login");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return http.build();
    }
}
