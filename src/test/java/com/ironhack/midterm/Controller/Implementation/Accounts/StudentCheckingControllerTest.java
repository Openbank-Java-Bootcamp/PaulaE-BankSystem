package com.ironhack.midterm.Controller.Implementation.Accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm.DTO.AccountsDTO.BalanceOnlyDTO;
import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Accounts.StudentChecking;
import com.ironhack.midterm.Model.Class.Address;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Model.Users.Role;
import com.ironhack.midterm.Model.Users.ThirdParty;
import com.ironhack.midterm.Repository.Accounts.StudentsCheckingRepository;
import com.ironhack.midterm.Repository.Users.AccountHolderRepository;
import com.ironhack.midterm.Repository.Users.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class StudentCheckingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    RoleRepository roleRepository;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    StudentsCheckingRepository studentsCheckingRepository;

    Collection<Role> roles;
    AccountHolder accountHolder;
    Address address;
    StudentChecking studentChecking;

    @BeforeEach
    void setUp() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        //roles
        roles = new ArrayList<>();
        Role rs = new Role("THIRD_PARTY");
        roleRepository.save(rs);
        Role s = new Role("ACCOUNT_HOLDER");
        roles.add(s);
        roleRepository.save(s);

        //accountholder
        address = new Address("Narbutta", "Madrid", "Spain", "000");
        accountHolder = new AccountHolder("Charo", roles,
                new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"), address, "añlkdsf","111", "pi" );
        accountHolderRepository.save(accountHolder);

        studentChecking = new StudentChecking(new Money(new BigDecimal(300)),accountHolder, null, "111" );
        studentsCheckingRepository.save(studentChecking);
    }

    @AfterEach
    void tearDown() {
        studentsCheckingRepository.deleteAll();
        accountHolderRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @WithMockUser(username = "Pi", password = "111", roles = "ADMIN")
    @Test
    void getBalanceById() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/studentcheckings/{id}", studentChecking.getId()))
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("300"));
    }

    @Test
    void updateBalance() throws Exception {
        BalanceOnlyDTO balanceOnlyDTO = new BalanceOnlyDTO();
        balanceOnlyDTO.setBalance(new Money(new BigDecimal(400)));

        String body = objectMapper.writeValueAsString(balanceOnlyDTO);
        MvcResult result = mockMvc.perform(patch("/api/studentcheckings/{id}", studentChecking.getId())
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    void deleteStudentChecking() throws Exception{
        MvcResult result = mockMvc.perform(delete("/api/studentcheckings/{id}", studentChecking.getId()))
                .andExpect(status().isNoContent()).andReturn();
        assertEquals(0, studentsCheckingRepository.findAll().size());
    }
}