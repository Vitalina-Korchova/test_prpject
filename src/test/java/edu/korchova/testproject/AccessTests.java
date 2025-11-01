package edu.korchova.testproject;

/*
    @author Віталіна
    @project test_prpject
    @class AccessTests
    @version 1.0.0
    @since 01.11.2025 - 13-26
*/

import edu.korchova.testproject.model.Doctor;
import edu.korchova.testproject.service.DoctorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class AccessTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void beforeAll() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }
    @MockBean
    private DoctorService doctorService;


    @Test
    @WithAnonymousUser
    public void whenAnonymUserThenStatusUnautorized() throws Exception {

        mockMvc.perform(get("/api/v1/doctors"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void whenAuthenticatedThenStatusOk() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/hello/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void whenUserGetAllDoctors_ThenStatusOk() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin",password = "admin", roles = {"ADMIN"})
    void whenAdminTriesToGetAllDoctors_ThenStatusOk() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user",password = "user",  roles = {"USER"})
    void whenUserTriesToGetDoctorById_ThenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void whenAdminTriesToGetDoctorById_ThenStatusOk() throws Exception {
        String doctorId = "68ea7443fe66ed055f4db768";
        Doctor mockDoctor = new Doctor(doctorId, "Test Doctor", "Cardiology");

        when(doctorService.getById(doctorId)).thenReturn(mockDoctor);

        mockMvc.perform(get("/api/v1/doctors/" + doctorId))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void whenAdminTriesToSayHelloAdmin_ThenStatusOk() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/hello/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user",password = "user", roles = {"USER"})
    void whenUserTriesToSayHelloAdmin_ThenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/hello/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "superadmin",password = "superadmin", roles = {"SUPERADMIN"})
    void whenSuperadminTriesToSayHelloSuperadmin_ThenStatusOk() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/hello/superadmin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void whenAdminTriesToSayHelloSuperadmin_ThenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/hello/superadmin"))
                .andExpect(status().isForbidden());
    }



}