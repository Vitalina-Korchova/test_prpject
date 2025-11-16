package edu.korchova.testproject.auth;

/*
    @author Віталіна
    @project test_prpject
    @class AuthenticationRequest
    @version 1.0.0
    @since 15.11.2025 - 21-18
*/
import lombok.Data;
import lombok.NonNull;
@Data
public class AuthenticationRequest {

    private String email;
    private String password;;
}