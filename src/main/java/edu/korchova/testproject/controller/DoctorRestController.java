package edu.korchova.testproject.controller;

/*
    @author Віталіна
    @project testProject
    @class DoctorRestController
    @version 1.0.0
    @since 03.04.2025 - 19-58
*/

import edu.korchova.testproject.model.Doctor;
import edu.korchova.testproject.request.DoctorCreateRequest;
import edu.korchova.testproject.request.DoctorUpdateRequest;
import edu.korchova.testproject.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/doctors/")
@RequiredArgsConstructor
public class DoctorRestController {

    private final DoctorService doctorService;

    // read all
    @GetMapping
    public List<Doctor> showAll() {
        return doctorService.getAll();
    }

    // read one
    @GetMapping("{id}")
    public Doctor showOneById(@PathVariable String id) {
        return doctorService.getById(id);
    }

    @PostMapping
    public Doctor insert(@RequestBody Doctor doctor) {
        return doctorService.create(doctor);
    }

    @PutMapping
    public Doctor edit(@RequestBody Doctor doctor) {
        return doctorService.update(doctor);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        doctorService.delById(id);
    }


    //dto
    @PostMapping("/dto")
    public Doctor insert(@RequestBody DoctorCreateRequest request) {
        return doctorService.create(request);
    }

    @PutMapping("/dto")
    public Doctor edit(@RequestBody DoctorUpdateRequest request) {
        return doctorService.update(request);
    }


}
