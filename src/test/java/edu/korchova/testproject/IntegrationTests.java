package edu.korchova.testproject;

/*
    @author Віталіна
    @project testProject
    @class IntegrationTests
    @version 1.0.0
    @since 15.05.2025 - 15-48
*/

import edu.korchova.testproject.model.Doctor;
import edu.korchova.testproject.repository.DoctorRepository;
import edu.korchova.testproject.request.DoctorCreateRequest;
import edu.korchova.testproject.request.DoctorUpdateRequest;
import edu.korchova.testproject.utils.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DoctorRepository repository;
    private List<Doctor> doctors = new ArrayList<>();

    @BeforeEach
    void setUp() {

        doctors.add(new Doctor("5", "Alice", "dermatologist", "text"));
        doctors.add(new Doctor("6", "Max", "neurologist", "text"));
        doctors.add(new Doctor("7", "Lisa", "pediatrician", "text"));
        doctors.forEach(doctor -> {
            if (doctor.getUpdateDate() == null) {
                doctor.setUpdateDate(new ArrayList<>());
            }
        });
        repository.saveAll(doctors);
    }

    @AfterEach
    void tearsDown(){
        repository.deleteAll();
    }


    @Test
    void itShouldCreateNewDoctor() throws Exception {
        // given
        DoctorCreateRequest request = new DoctorCreateRequest(
                "Margo", "cardiologist", "text");
        // when
        ResultActions perform = mockMvc.perform(post("http://localhost:8080/api/v1/doctors/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(request)));

        //then
        Doctor doctor = repository.findAll()
                .stream()
                .filter(it -> it.getSpecialization().equals(request.specialization()))
                .findFirst().orElse(null);

        perform.andExpect(status().isOk());
        assertThat(repository.existsBySpecialization(request.specialization())).isTrue();
        assertNotNull(doctor);
        assertNotNull(doctor.getId());
        assertThat(doctor.getId()).isNotEmpty();
        assertThat(doctor.getId().length()).isEqualTo(24);
        assertThat(doctor.getDescription()).isEqualTo(request.description());
        assertThat(doctor.getName()).isEqualTo(request.name());
        assertThat(doctor.getSpecialization()).isEqualTo(request.specialization());
        assertThat(doctor.getUpdateDate()).isEmpty();
        assertThat(doctor.getCreateDate()).isNotNull();
    }

    @Test
    void itShouldNotCreateDoctorWithExistingSpecialization() throws Exception {
        DoctorCreateRequest request = new DoctorCreateRequest("Another Name", "dermatologist", "some text");

        mockMvc.perform(post("/api/v1/doctors/dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void itShouldUpdateDoctorSuccessfully() throws Exception {
        Doctor existing = doctors.get(0);

        DoctorUpdateRequest updateRequest = new DoctorUpdateRequest(
                existing.getId(), "Updated Name", "updated-spec", "updated-desc");

        mockMvc.perform(put("/api/v1/doctors/dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.toJson(updateRequest)))
                .andExpect(status().isOk());

        Doctor updated = repository.findById(existing.getId()).orElseThrow();

        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getSpecialization()).isEqualTo("updated-spec");
        assertThat(updated.getDescription()).isEqualTo("updated-desc");
    }

    @Test
    void itShouldFailToUpdateNonexistentDoctor() throws Exception {
        DoctorUpdateRequest request = new DoctorUpdateRequest(
                "000000000000000000000000", "Ghost", "ghost-spec", "ghost-desc");

        mockMvc.perform(put("/api/v1/doctors/dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.toJson(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void itShouldReturnDoctorById() throws Exception {
        Doctor existing = doctors.get(1);

        mockMvc.perform(get("/api/v1/doctors/{id}", existing.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existing.getId()))
                .andExpect(jsonPath("$.name").value(existing.getName()))
                .andExpect(jsonPath("$.specialization").value(existing.getSpecialization()));
    }

    @Test
    void itShouldReturnNotFoundWhenGettingNonexistentDoctor() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/{id}", "000000000000000000000000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void itShouldDeleteDoctorSuccessfully() throws Exception {
        Doctor toDelete = doctors.get(2);

        mockMvc.perform(delete("/api/v1/doctors/{id}", toDelete.getId()))
                .andExpect(status().isNoContent());

        assertThat(repository.findById(toDelete.getId())).isEmpty();
    }

    @Test
    void itShouldFailToDeleteNonexistentDoctor() throws Exception {
        mockMvc.perform(delete("/api/v1/doctors/{id}", "000000000000000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void itShouldReturnAllDoctors() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(doctors.size()));
    }

    @Test
    void itShouldNotUpdateDoctorWithDuplicateSpecialization() throws Exception {
        Doctor existing = doctors.get(0); // "dermatologist"
        Doctor targetToUpdate = doctors.get(1); // "neurologist"

        DoctorUpdateRequest request = new DoctorUpdateRequest(
                targetToUpdate.getId(),
                "Updated Name",
                "dermatologist",
                "Updated desc"
        );

        mockMvc.perform(put("/api/v1/doctors/dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.toJson(request)))
                .andExpect(status().isBadRequest());

    }




}
