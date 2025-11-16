package edu.korchova.testproject.auth;

/*
    @author Віталіна
    @project test_prpject
    @class AuthenticationResponse
    @version 1.0.0
    @since 15.11.2025 - 21-19
*/import lombok.*;

@Builder
@Getter
@Setter
public class AuthenticationResponse {
    private String token;
}