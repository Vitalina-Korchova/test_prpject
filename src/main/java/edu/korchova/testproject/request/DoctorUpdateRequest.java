package edu.korchova.testproject.request;

/*
    @author Віталіна
    @project testProject
    @class DoctorUpdateRequest
    @version 1.0.0
    @since 27.04.2025 - 14-23
*/
public record DoctorUpdateRequest(String id,String name, String specialization, String description)  {
}
