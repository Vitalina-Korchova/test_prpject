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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
@Builder
public class Doctor extends AuditMetadata {
    @Id
    private String id;
    private String name;
    private String specialization;
    private String description;

    private LocalDateTime createDate;
    private List<LocalDateTime> updateDate;

    public Doctor(String name, String specialization, String description) {
        this.name = name;
        this.specialization = specialization;
        this.description = description;
    }

    public Doctor(String id, String name, String specialization, String description) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.description = description;
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
