package edu.korchova.testproject.utils;

/*
    @author Віталіна
    @project testProject
    @class Utils
    @version 1.0.0
    @since 15.05.2025 - 15-56
*/

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    public static String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

}