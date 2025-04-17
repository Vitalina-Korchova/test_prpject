package edu.korchova.testproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/*
    @author Віталіна
    @project testProject
    @class AuditionConfiguration
    @version 1.0.0
    @since 17.04.2025 - 17-09
*/
@EnableMongoAuditing
@Configuration
public class AuditionConfiguration {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
}
