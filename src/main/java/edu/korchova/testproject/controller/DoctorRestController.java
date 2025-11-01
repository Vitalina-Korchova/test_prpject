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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors/")
@RequiredArgsConstructor
public class DoctorRestController {

    private final DoctorService doctorService;

    // read all
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','SUPERADMIN')")
    @GetMapping
    public ResponseEntity<List<Doctor>> showAll() {
        return ResponseEntity.ok(doctorService.getAll());
    }

    // read one
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<?> showOneById(@PathVariable String id) {
        Doctor doctor = doctorService.getById(id);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
        return ResponseEntity.ok(doctor);
    }

    @PostMapping
    public ResponseEntity<Doctor> insert(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.create(doctor));
    }

    @PutMapping
    public ResponseEntity<Doctor> edit(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.update(doctor));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Doctor doctor = doctorService.getById(id);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
        doctorService.delById(id);
        return ResponseEntity.noContent().build();
    }

    // DTO create
    @PostMapping("/dto")
    public ResponseEntity<?> insert(@RequestBody DoctorCreateRequest request) {
        try {
            Doctor created = doctorService.create(request);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DTO update
    @PutMapping("/dto")
    public ResponseEntity<?> edit(@RequestBody DoctorUpdateRequest request) {
        try {
            Doctor updated = doctorService.update(request);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Doctor not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/hello/user")
    public String helloUser() {
        return "Hello User!";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("hello/admin")
    public String helloAdmin() {
        return "Hello Admin!";
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("hello/superadmin")
    public String helloSuperadmin() {
        return "Hello Superadmin!";
    }




}

