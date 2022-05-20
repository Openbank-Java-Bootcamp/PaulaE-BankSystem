package com.ironhack.midterm.Controller.Implementation.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm.DTO.UsersDTO.AdminDTO;
import com.ironhack.midterm.Model.Users.Admin;
import com.ironhack.midterm.Model.Users.Role;
import com.ironhack.midterm.Repository.Users.AdminRepository;
import com.ironhack.midterm.Repository.Users.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AdminControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    RoleRepository roleRepository;

    Collection<Role> roles;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        roles = new ArrayList<>();
        Role r = new Role("ADMIN");
        roles.add(r);
        roleRepository.save(r);
    }

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
        roleRepository.deleteAll();
    }


    @Test
    public void saveAdmin_WORKS() throws Exception{
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setName("PEPE");
        adminDTO.setPassword("1111");
        adminDTO.setUsername("pppp");
        String body = objectMapper.writeValueAsString(adminDTO);
        MvcResult result = mockMvc.perform(post("/api/admins").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
    }

    @Test
    public void deleteAdmin_WORKS() throws Exception{
        Admin admin = new Admin("PEPE",roles, "1234", "pepe");
        adminRepository.save(admin);
        MvcResult result = mockMvc.perform(delete("/api/admins/{id}", admin.getId()))
                .andExpect(status().isNoContent()).andReturn();
    }

}