package edu.korchova.testproject.service;

/*
    @author Віталіна
    @project testProject
    @class DoctorServiceMockTests
    @version 1.0.0
    @since 07.05.2025 - 20-10
*/

import edu.korchova.testproject.model.Doctor;
import edu.korchova.testproject.repository.DoctorRepository;
import edu.korchova.testproject.request.DoctorCreateRequest;
import edu.korchova.testproject.request.DoctorUpdateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class DoctorServiceMockTests {

    @Mock
    private DoctorRepository mockRepository;

    private DoctorService underTest;

    @Captor
    private ArgumentCaptor<Doctor> argumentCaptor;

    private DoctorCreateRequest request;
    private Doctor doctor;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new DoctorService(mockRepository);
        argumentCaptor = ArgumentCaptor.forClass(Doctor.class);
    }

    @AfterEach
    void tearsDown() {

    }

    @DisplayName("Create new Doctor. Happy Path")
    @Test
    void whenInsertNewDoctorAndSpecializationNotExistsThenOk() {
        //given
        request = new DoctorCreateRequest("Lili", "gynecologist", "text");
        doctor = Doctor.builder()
                .name(request.name())
                .specialization(request.specialization())
                .description(request.description())
                .build();
        given(mockRepository.existsBySpecialization(request.specialization())).willReturn(false);
        // when
        underTest.create(request);
        // then
        then(mockRepository).should().save(argumentCaptor.capture());
        Doctor itemToSave = argumentCaptor.getValue();
        assertThat(itemToSave.getName()).isEqualTo(request.name());
        assertNotNull(itemToSave.getCreateDate());
        assertTrue(itemToSave.getCreateDate().isBefore(LocalDateTime.now()));
        assertTrue(itemToSave.getUpdateDate().isEmpty());
        verify(mockRepository).save(itemToSave);
        verify(mockRepository, times(1)).existsBySpecialization(request.specialization());
        verify(mockRepository, times(1)).save(itemToSave);
    }

    @Test
    @DisplayName("Update existing doctor. Happy path")
    void updateDoctor_whenValid_thenUpdated() {
        // given
        String id = "1";
        LocalDateTime created = LocalDateTime.of(2024, 1, 1, 12, 0);
        List<LocalDateTime> updates = new ArrayList<>();
        updates.add(LocalDateTime.of(2025, 1, 1, 12, 0));

        Doctor existing = Doctor.builder()
                .id(id)
                .name("Alice")
                .specialization("dentist")
                .description("desc")
                .createDate(created)
                .updateDate(updates)
                .build();

        DoctorUpdateRequest request = new DoctorUpdateRequest(
                id,
                "Alice Updated",
                "phthisiologist",
                "new desc"
        );

        given(mockRepository.existsBySpecialization(request.specialization())).willReturn(false);
        given(mockRepository.findById(id)).willReturn(Optional.of(existing));
        given(mockRepository.save(any(Doctor.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Doctor result = underTest.update(request);

        // then
        verify(mockRepository).save(argumentCaptor.capture());
        Doctor saved = argumentCaptor.getValue();

        assertThat(saved.getId()).isEqualTo(id);
        assertThat(saved.getName()).isEqualTo("Alice Updated");
        assertThat(saved.getSpecialization()).isEqualTo("phthisiologist");
        assertThat(saved.getDescription()).isEqualTo("new desc");
        assertThat(saved.getCreateDate()).isEqualTo(created);
        assertThat(saved.getUpdateDate()).hasSize(2);
        assertThat(result).isEqualTo(saved);
    }

    @Test
    @DisplayName("Do not update if specialization already exists")
    void updateDoctor_whenSpecializationExists_thenReturnNull() {
        // given
        DoctorUpdateRequest request = new DoctorUpdateRequest(
                "99", "John", "therapist", "desc"
        );
        given(mockRepository.existsBySpecialization("therapist")).willReturn(true);

        // when
        Doctor result = underTest.update(request);

        // then
        verify(mockRepository, never()).findById(any());
        verify(mockRepository, never()).save(any());
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Do not update if doctor not found by ID")
    void updateDoctor_whenNotFound_thenReturnNull() {
        // given
        DoctorUpdateRequest request = new DoctorUpdateRequest(
                "nonexistent-id", "Tom", "dermatologist", "desc"
        );
        given(mockRepository.existsBySpecialization("dermatologist")).willReturn(false);
        given(mockRepository.findById("nonexistent-id")).willReturn(Optional.empty());

        // when
        Doctor result = underTest.update(request);

        // then
        verify(mockRepository, never()).save(any());
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Do not create doctor if specialization exists")
    void whenCreateDoctorWithExistingSpecialization_thenNoSave() {
        DoctorCreateRequest request = new DoctorCreateRequest("Anna", "cardiologist", "desc");
        given(mockRepository.existsBySpecialization("cardiologist")).willReturn(true);

        Doctor result = underTest.create(request);

        assertNull(result);
        verify(mockRepository, never()).save(any());
    }


    @Test
    @DisplayName("Update with same data should still update timestamp")
    void updateDoctor_whenSameData_thenUpdateTimestamp() {
        String id = "2";
        LocalDateTime created = LocalDateTime.of(2023, 5, 5, 10, 0);
        List<LocalDateTime> updates = new ArrayList<>();

        Doctor existing = Doctor.builder()
                .id(id)
                .name("Bob")
                .specialization("neurologist")
                .description("desc")
                .createDate(created)
                .updateDate(updates)
                .build();

        DoctorUpdateRequest request = new DoctorUpdateRequest(
                id, "Bob", "neurologist", "desc"
        );

        given(mockRepository.existsBySpecialization("neurologist")).willReturn(false);
        given(mockRepository.findById(id)).willReturn(Optional.of(existing));
        given(mockRepository.save(any(Doctor.class))).willAnswer(invocation -> invocation.getArgument(0));

        Doctor result = underTest.update(request);

        assertThat(result.getUpdateDate()).hasSize(1);
    }


    @Test
    @DisplayName("Create doctor with null values in request")
    void createDoctor_whenNullValues_thenHandleAppropriately() {
        // given
        DoctorCreateRequest request = new DoctorCreateRequest(null, "pediatrician", null);
        doctor = Doctor.builder()
                .name(request.name())
                .specialization(request.specialization())
                .description(request.description())
                .build();
        given(mockRepository.existsBySpecialization("pediatrician")).willReturn(false);

        // when
        underTest.create(request);

        // then
        verify(mockRepository).save(argumentCaptor.capture());
        Doctor captured = argumentCaptor.getValue();
        assertThat(captured.getName()).isNull();
        assertThat(captured.getSpecialization()).isEqualTo("pediatrician");
        assertThat(captured.getDescription()).isNull();
        assertNotNull(captured.getCreateDate());
    }

    @Test
    @DisplayName("Update doctor with only description changed")
    void updateDoctor_whenOnlyDescriptionChanged_thenUpdate() {
        String id = "3";
        LocalDateTime created = LocalDateTime.of(2024, 6, 1, 9, 0);
        List<LocalDateTime> updates = new ArrayList<>();

        Doctor existing = Doctor.builder()
                .id(id)
                .name("Carol")
                .specialization("surgeon")
                .description("old desc")
                .createDate(created)
                .updateDate(updates)
                .build();

        DoctorUpdateRequest request = new DoctorUpdateRequest(
                id, "Carol", "surgeon", "new desc"
        );

        given(mockRepository.existsBySpecialization("surgeon")).willReturn(false);
        given(mockRepository.findById(id)).willReturn(Optional.of(existing));
        given(mockRepository.save(any(Doctor.class))).willAnswer(invocation -> invocation.getArgument(0));

        Doctor result = underTest.update(request);

        verify(mockRepository).save(argumentCaptor.capture());
        Doctor updated = argumentCaptor.getValue();

        assertEquals("new desc", updated.getDescription());
        assertEquals("Carol", updated.getName());
        assertEquals("surgeon", updated.getSpecialization());
        assertThat(updated.getUpdateDate()).hasSize(1);
        assertEquals(result, updated);
    }

    @Test
    @DisplayName("Update doctor setting description to null")
    void updateDoctor_whenNullDescription_thenUpdatedWithNull() {
        // given
        String id = "3";
        LocalDateTime created = LocalDateTime.of(2023, 1, 1, 10, 0);
        List<LocalDateTime> updates = new ArrayList<>();

        Doctor existing = Doctor.builder()
                .id(id)
                .name("Kate")
                .specialization("oncologist")
                .description("initial desc")
                .createDate(created)
                .updateDate(updates)
                .build();

        DoctorUpdateRequest request = new DoctorUpdateRequest(
                id, "Kate", "oncologist", null
        );

        given(mockRepository.existsBySpecialization("oncologist")).willReturn(false);
        given(mockRepository.findById(id)).willReturn(Optional.of(existing));
        given(mockRepository.save(any(Doctor.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Doctor result = underTest.update(request);

        // then
        assertThat(result.getDescription()).isNull();
        assertThat(result.getUpdateDate()).hasSize(1);
    }

    @Test
    @DisplayName("Create doctor with empty string values")
    void createDoctor_whenEmptyStrings_thenCreateWithEmptyStrings() {
        // given
        DoctorCreateRequest request = new DoctorCreateRequest("", "dermatologist", "");
        doctor = Doctor.builder()
                .name(request.name())
                .specialization(request.specialization())
                .description(request.description())
                .build();
        given(mockRepository.existsBySpecialization("dermatologist")).willReturn(false);
        given(mockRepository.save(any(Doctor.class))).willAnswer(invocation -> {
            Doctor savedDoctor = invocation.getArgument(0);
            savedDoctor.setId("generated-id");
            return savedDoctor;
        });

        // when
        Doctor result = underTest.create(request);

        // then
        verify(mockRepository).save(argumentCaptor.capture());
        Doctor captured = argumentCaptor.getValue();

        assertThat(captured.getName()).isEqualTo("");
        assertThat(captured.getSpecialization()).isEqualTo("dermatologist");
        assertThat(captured.getDescription()).isEqualTo("");
        assertNotNull(captured.getCreateDate());
        assertThat(captured.getUpdateDate()).isEmpty();

        assertNotNull(result);
        assertThat(result.getId()).isEqualTo("generated-id");
    }


}
