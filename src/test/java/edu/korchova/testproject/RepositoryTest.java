package edu.korchova.testproject;

/*
    @author Віталіна
    @project testProject
    @class RepositoryTest
    @version 1.0.0
    @since 17.04.2025 - 15-54
*/

import edu.korchova.testproject.model.Doctor;
import edu.korchova.testproject.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import javax.print.Doc;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataMongoTest
public class RepositoryTest {

    @Autowired
    DoctorRepository underTest;

    @BeforeEach
    void setUp() {
        Doctor doc1 = new Doctor("1", "Alice", "surgeon", "###test");
        Doctor doc2 = new Doctor("2", "Mick", "therapist", "###test");
        Doctor doc3 = new Doctor("3", "Helen", "ophthalmologist", "###test");

        underTest.saveAll(List.of(doc1, doc2, doc3));
    }

    @AfterEach
    void tearDown() {
        List<Doctor> doctorsToDelete = underTest.findAll().stream()
                .filter(item -> item.getDescription().contains("###test"))
                .toList();
        underTest.deleteAll(doctorsToDelete);
    }


    @Test
    void testSetShouldContains_3_Records_ToTest(){
        List<Doctor> doctorsToDelete = underTest.findAll().stream()
                .filter(item -> item.getDescription().contains("###test"))
                .toList();
        assertEquals(3,doctorsToDelete.size());
    }

    @Test
    void shouldGiveIdForNewRecord() {

        Doctor docNew = new Doctor( "Anna", "ophthalmologist", "###test");

        underTest.save(docNew);
        Doctor itemFromDb = underTest.findAll().stream()
                .filter(item -> item.getName().equals("Anna"))
                .findFirst().orElse(null);

        assertFalse(itemFromDb.getId() == docNew.getId());
        assertNotNull(itemFromDb);
        assertNotNull(itemFromDb.getId());
        assertFalse(itemFromDb.getId().isEmpty());
        assertEquals(24, itemFromDb.getId().length());
    }

    @Test
    void shouldDeleteDoctor() {
        Doctor doc = new Doctor("Eve", "therapist", "###test");
        Doctor saved = underTest.save(doc);
        underTest.deleteById(saved.getId());
        assertFalse(underTest.findById(saved.getId()).isPresent());
    }


    @Test
    void shouldUpdateDoctor() {
        Doctor doc = underTest.save(new Doctor("Kate", "dentist", "###test"));
        doc.setSpecialization("surgeon");
        underTest.save(doc);
        Doctor updated = underTest.findById(doc.getId()).orElse(null);
        assertEquals("surgeon", updated.getSpecialization());
    }

    @Test
    void shouldNotSetAuditFields_WhenSavedDirectlyThroughRepository() {
        // given
        Doctor doctor = new Doctor("Bob", "cardiologist", "###test");

        // when
        underTest.save(doctor);

        // then
        Doctor saved = underTest.findAll().stream()
                .filter(d -> d.getName().equals("Bob"))
                .findFirst()
                .orElse(null);

        assertNotNull(saved);

        //поля аудиту мають бути null
        assertNull(saved.getCreatedDate(), "Expected createdDate to be null when saved directly through repository");
        assertNull(saved.getCreatedBy(), "Expected createdBy to be null when saved directly through repository");
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingSpecialization() {
        List<Doctor> notFound = underTest.findAll().stream()
                .filter(d -> d.getSpecialization().equals("dermatologist"))
                .toList();

        assertTrue(notFound.isEmpty());
    }

    @Test
    void shouldGenerateUniqueIdForEachDoctor() {
        Doctor d1 = new Doctor("Greg", "cardiologist", "###test");
        Doctor d2 = new Doctor("Monica", "urologist", "###test");

        underTest.saveAll(List.of(d1, d2));

        List<Doctor> saved = underTest.findAll().stream()
                .filter(d -> d.getDescription().equals("###test"))
                .toList();

        assertNotEquals(saved.get(0).getId(), saved.get(1).getId());
    }

    @Test
    void shouldFindDoctorsBySpecialization() {
        // given
        String specialization = "ophthalmologist";

        // when
        List<Doctor> ophthalmologists = underTest.findAll().stream()
                .filter(d -> d.getSpecialization().equals(specialization))
                .toList();

        // then
        assertEquals(1, ophthalmologists.size());
        assertEquals("Helen", ophthalmologists.get(0).getName());
    }

    @Test
    void shouldCountTotalDoctorsWithTestDescription() {
        // given
        long initialCount = underTest.findAll().stream()
                .filter(d -> d.getDescription().contains("###test"))
                .count();

        // when
        Doctor newDoctor = new Doctor("Patricia", "psychiatrist", "###test");
        underTest.save(newDoctor);

        // then
        long newCount = underTest.findAll().stream()
                .filter(d -> d.getDescription().contains("###test"))
                .count();

        assertEquals(initialCount + 1, newCount);
    }

    @Test
    void shouldFindDoctorByIdAndVerifyAllFields() {
        // given
        Doctor newDoctor = new Doctor("Richard", "anesthesiologist", "###test");
        Doctor saved = underTest.save(newDoctor);
        String id = saved.getId();

        // when
        Doctor found = underTest.findById(id).orElse(null);

        // then
        assertNotNull(found);
        assertEquals("Richard", found.getName());
        assertEquals("anesthesiologist", found.getSpecialization());
        assertEquals("###test", found.getDescription());
    }



}
