package edu.korchova.testproject.model;

/*
    @author Віталіна
    @project testProject
    @class Doctor
    @version 1.0.0
    @since 03.04.2025 - 19-06
*/

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
public class Doctor {
    @Id
    private String id;
    private String name;
    private String code;
    private String specialization;

    public Doctor(String name, String code, String specialization) {
        this.name = name;
        this.code = code;
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor doctor)) return false;
        return Objects.equals(getId(), doctor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
