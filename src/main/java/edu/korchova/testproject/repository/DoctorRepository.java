package edu.korchova.testproject.repository;

import edu.korchova.testproject.model.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/*
    @author Віталіна
    @project testProject
    @class DoctorRepository
    @version 1.0.0
    @since 03.04.2025 - 19-11
*/

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {
    public boolean existsBySpecialization(String specialization);
}
