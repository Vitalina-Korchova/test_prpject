package edu.korchova.testproject.user;

/*
    @author Віталіна
    @project test_prpject
    @class UserRepository
    @version 1.0.0
    @since 16.11.2025 - 16-32
*/

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}