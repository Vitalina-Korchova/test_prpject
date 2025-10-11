package edu.korchova.testproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/*
    @author Віталіна
    @project test_prpject
    @class SecurityConfig
    @version 1.0.0
    @since 11.10.2025 - 18-07
*/
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf ->csrf.disable())
                .authorizeHttpRequests( auth -> auth.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
