package edu.korchova.testproject.model;
import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
/*
    @author Віталіна
    @project testProject
    @class AuditMetadata
    @version 1.0.0
    @since 17.04.2025 - 17-10
*/

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class AuditMetadata {

    @CreatedDate
    @NonNull
    private LocalDateTime createdDate;
    @CreatedBy
    @NonNull
    private String createdBy;
    @LastModifiedDate
    @Nullable
    private LocalDateTime lastModifiedDate;
    @Nullable
    @LastModifiedBy
    private String lastModifiedBy;
}
