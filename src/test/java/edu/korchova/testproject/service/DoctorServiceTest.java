package edu.korchova.testproject.service;

import edu.korchova.testproject.model.Doctor;
import edu.korchova.testproject.repository.DoctorRepository;
import edu.korchova.testproject.request.DoctorCreateRequest;
import edu.korchova.testproject.request.DoctorUpdateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/*
    @author Віталіна
    @project testProject
    @class DoctorServiceTest
    @version 1.0.0
    @since 27.04.2025 - 14-41
*/
@SpringBootTest
class DoctorServiceTest {

    @Autowired
    private DoctorRepository repository;

    @Autowired
    private DoctorService underTest;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void createDoctor_ShouldReturnNonNullDoctor() {
        DoctorCreateRequest request = new DoctorCreateRequest("Lina", "therapist", "description333");
        Doctor createdDoctor = underTest.create(request);
        assertNotNull(createdDoctor);
    }

    @Test
    void createDoctor_ShouldGenerateId() {
        DoctorCreateRequest request = new DoctorCreateRequest("Lina", "therapist", "description333");
        Doctor createdDoctor = underTest.create(request);
        assertNotNull(createdDoctor.getId());
    }

    @Test
    void createDoctor_ShouldSetCorrectName() {
        DoctorCreateRequest request = new DoctorCreateRequest("Lina", "therapist", "description333");
        Doctor createdDoctor = underTest.create(request);
        assertEquals("Lina", createdDoctor.getName());
    }

    @Test
    void createDoctor_ShouldSetCorrectSpecialization() {
        DoctorCreateRequest request = new DoctorCreateRequest("Lina", "therapist", "description333");
        Doctor createdDoctor = underTest.create(request);
        assertEquals("therapist", createdDoctor.getSpecialization());
    }

    @Test
    void createDoctor_ShouldSetCorrectDescription() {
        DoctorCreateRequest request = new DoctorCreateRequest("Lina", "therapist", "description333");
        Doctor createdDoctor = underTest.create(request);
        assertEquals("description333", createdDoctor.getDescription());
    }

    @Test
    void createDoctor_ShouldSetCreateDate() {
        DoctorCreateRequest request = new DoctorCreateRequest("Lina", "therapist", "description333");
        Doctor createdDoctor = underTest.create(request);
        assertNotNull(createdDoctor.getCreateDate());
    }

    @Test
    void createDoctor_ShouldSetRecentCreateDate() {
        LocalDateTime now = LocalDateTime.now();
        DoctorCreateRequest request = new DoctorCreateRequest("Lina", "therapist", "description333");
        Doctor createdDoctor = underTest.create(request);
        assertTrue(createdDoctor.getCreateDate().isAfter(now.minusSeconds(1)));
    }

    @Test
    void createDoctor_CreateDateShouldBeLocalDateTime() {
        DoctorCreateRequest request = new DoctorCreateRequest("Lina", "therapist", "description333");
        Doctor createdDoctor = underTest.create(request);
        assertSame(LocalDateTime.class, createdDoctor.getCreateDate().getClass());
    }

    @Test
    void createDoctor_ShouldInitializeUpdateDate() {
        DoctorCreateRequest request = new DoctorCreateRequest("Lina", "therapist", "description333");
        Doctor createdDoctor = underTest.create(request);
        assertNotNull(createdDoctor.getUpdateDate());
    }

    @Test
    void createDoctor_UpdateDateShouldBeArrayList() {
        DoctorCreateRequest request = new DoctorCreateRequest("Lina", "therapist", "description333");
        Doctor createdDoctor = underTest.create(request);
        assertSame(ArrayList.class, createdDoctor.getUpdateDate().getClass());
    }

    @Test
    void createDoctor_UpdateDateShouldBeEmpty() {
        DoctorCreateRequest request = new DoctorCreateRequest("Lina", "therapist", "description333");
        Doctor createdDoctor = underTest.create(request);
        assertTrue(createdDoctor.getUpdateDate().isEmpty());
    }


    @Test
    void updateDoctor_ShouldReturnNonNullDoctor() {
        Doctor createdDoctor = createTestDoctor();
        DoctorUpdateRequest updateRequest = new DoctorUpdateRequest(
                createdDoctor.getId(), "UpdatedVova", "orthopedist", "updated description");
        Doctor updatedDoctor = underTest.update(updateRequest);
        assertNotNull(updatedDoctor);
    }

    @Test
    void updateDoctor_ShouldUpdateName() {
        Doctor createdDoctor = createTestDoctor();
        DoctorUpdateRequest updateRequest = new DoctorUpdateRequest(
                createdDoctor.getId(), "UpdatedVova", "orthopedist", "updated description");
        Doctor updatedDoctor = underTest.update(updateRequest);
        assertEquals("UpdatedVova", updatedDoctor.getName());
    }

    @Test
    void updateDoctor_ShouldUpdateSpecialization() {
        Doctor createdDoctor = createTestDoctor();
        DoctorUpdateRequest updateRequest = new DoctorUpdateRequest(
                createdDoctor.getId(), "UpdatedVova", "orthopedist", "updated description");
        Doctor updatedDoctor = underTest.update(updateRequest);
        assertEquals("orthopedist", updatedDoctor.getSpecialization());
    }

    @Test
    void updateDoctor_ShouldUpdateDescription() {
        Doctor createdDoctor = createTestDoctor();
        DoctorUpdateRequest updateRequest = new DoctorUpdateRequest(
                createdDoctor.getId(), "UpdatedVova", "orthopedist", "updated description");
        Doctor updatedDoctor = underTest.update(updateRequest);
        assertEquals("updated description", updatedDoctor.getDescription());
    }

    @Test
    void updateDoctor_ShouldPreserveId() {
        Doctor createdDoctor = createTestDoctor();
        DoctorUpdateRequest updateRequest = new DoctorUpdateRequest(
                createdDoctor.getId(), "UpdatedVova", "orthopedist", "updated description");
        Doctor updatedDoctor = underTest.update(updateRequest);
        assertEquals(createdDoctor.getId(), updatedDoctor.getId());
    }

    @Test
    void updateDoctor_ShouldAddUpdateTimestamp() {
        Doctor createdDoctor = createTestDoctor();
        DoctorUpdateRequest updateRequest = new DoctorUpdateRequest(
                createdDoctor.getId(), "UpdatedVova", "orthopedist", "updated description");
        Doctor updatedDoctor = underTest.update(updateRequest);
        assertEquals(1, updatedDoctor.getUpdateDate().size());
    }

    @Test
    void updateDoctor_ShouldSetRecentUpdateTimestamp() {
        Doctor createdDoctor = createTestDoctor();
        LocalDateTime beforeUpdate = LocalDateTime.now();
        DoctorUpdateRequest updateRequest = new DoctorUpdateRequest(
                createdDoctor.getId(), "UpdatedVova", "orthopedist", "updated description");
        Doctor updatedDoctor = underTest.update(updateRequest);
        LocalDateTime lastUpdate = updatedDoctor.getUpdateDate().get(0);
        assertTrue(lastUpdate.isAfter(beforeUpdate) || lastUpdate.equals(beforeUpdate));
    }

    @Test
    void updateDoctor_ShouldKeepCreateDateBeforeUpdateDate() {
        Doctor createdDoctor = createTestDoctor();
        DoctorUpdateRequest updateRequest = new DoctorUpdateRequest(
                createdDoctor.getId(), "UpdatedVova", "orthopedist", "updated description");
        Doctor updatedDoctor = underTest.update(updateRequest);
        LocalDateTime lastUpdate = updatedDoctor.getUpdateDate().get(0);
        assertTrue(updatedDoctor.getCreateDate().isBefore(lastUpdate));
    }

    @Test
    void updateNonExistingDoctor_ShouldReturnNull() {

        DoctorUpdateRequest updateRequest = new DoctorUpdateRequest(
               "122",
                "NonExisting",
                "specialization",
                "description"
        );

        Doctor result = underTest.update(updateRequest);

        assertNull(result);
    }

    private Doctor createTestDoctor() {
        DoctorCreateRequest createRequest = new DoctorCreateRequest("Vova", "surgeon", "initial description");
        return underTest.create(createRequest);
    }

}