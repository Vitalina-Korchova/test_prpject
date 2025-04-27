package edu.korchova.testproject.service;

/*
    @author Віталіна
    @project testProject
    @class DoctorService
    @version 1.0.0
    @since 03.04.2025 - 19-12
*/

import edu.korchova.testproject.model.Doctor;
import edu.korchova.testproject.repository.DoctorRepository;
import edu.korchova.testproject.request.DoctorCreateRequest;
import edu.korchova.testproject.request.DoctorUpdateRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    private List<Doctor> doctors = new ArrayList<>(Arrays.asList(
            new Doctor("1", "Alice", "therapist", "text"),
            new Doctor("2", "Max", "ophthalmologist", "text"),
            new Doctor("3", "Lisa", "surgeon", "text")
            ));


//    @PostConstruct
//    void init(){
//        doctorRepository.deleteAll();
//        doctorRepository.saveAll(doctors);
//    }

    //Crud

    public List<Doctor> getAll(){
        return  doctorRepository.findAll();
    }
    public Doctor getById(String id){
        return doctorRepository.findById(id).orElse(null);
    }
    public Doctor create(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor create(DoctorCreateRequest request) {
        Doctor doctor = mapToDoctor(request);
        doctor.setCreateDate(LocalDateTime.now());
        doctor.setUpdateDate(new ArrayList<LocalDateTime>());
        return doctorRepository.save(doctor);
    }

    public  Doctor update(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor update(DoctorUpdateRequest request) {
        Doctor doctorPersisted = doctorRepository.findById(request.id()).orElse(null);
        if (doctorPersisted != null) {
            List<LocalDateTime> updateDates = doctorPersisted.getUpdateDate();
            updateDates.add(LocalDateTime.now());
            Doctor itemToUpdate =
                    Doctor.builder()
                            .id(request.id())
                            .name(request.name())
                            .specialization(request.specialization())
                            .description(request.description())
                            .createDate(doctorPersisted.getCreateDate())
                            .updateDate(updateDates)
                            .build();
            return doctorRepository.save(itemToUpdate);

        }
        return null;
    }

    public void delById(String id) {
        doctorRepository.deleteById(id);
    }

    private Doctor mapToDoctor(DoctorCreateRequest request) {
        Doctor doctor = new Doctor(request.name(), request.specialization(), request.description());
        return doctor;
    }
}
