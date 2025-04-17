package edu.korchova.testproject.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/*
    @author Віталіна
    @project testProject
    @class AuditorAwareImpl
    @version 1.0.0
    @since 17.04.2025 - 17-08
*/
public class AuditorAwareImpl  implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(System.getProperty("user.name"));
    }
}
