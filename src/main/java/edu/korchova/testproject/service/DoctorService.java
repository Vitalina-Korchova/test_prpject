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
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    private List<Doctor> doctors = new ArrayList<>(Arrays.asList(
            new Doctor("1", "Alice", "0000001", "therapist"),
            new Doctor("2", "Max", "0000002", "ophthalmologist"),
            new Doctor("3", "Lisa", "0000003", "surgeon")
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

    public  Doctor update(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void delById(String id) {
        doctorRepository.deleteById(id);
    }
}
