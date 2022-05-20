package com.ironhack.midterm.Controller.Implementation.Accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm.DTO.AccountsDTO.BalanceOnlyDTO;
import com.ironhack.midterm.DTO.AccountsDTO.SavingDTO;
import com.ironhack.midterm.DTO.UsersDTO.AccountHolderDTO;
import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Accounts.Savings;
import com.ironhack.midterm.Model.Class.Address;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Model.Users.Role;
import com.ironhack.midterm.Repository.Accounts.SavingsRepository;
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
class SavingsControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    SavingsRepository savingsRepository;

    Collection<Role> roles;
    AccountHolder accountHolder;
    Address address;

    Savings savings;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        //roles
        roles = new ArrayList<>();
        Role s = new Role("ACCOUNT_HOLDER");
        roles.add(s);
        roleRepository.save(s);

        //accountholder
        address = new Address("Narbutta", "Madrid", "Spain", "000");
        accountHolder = new AccountHolder("Charo", roles,
                new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"), address, "añlkdsf","111", "pi" );
        accountHolderRepository.save(accountHolder);

        //saving
        savings = new Savings(new Money(new BigDecimal(300)),accountHolder, null, "222", null );
        savingsRepository.save(savings);
    }

    @AfterEach
    void tearDown() {
        savingsRepository.deleteAll();
        savingsRepository.deleteAll();
        accountHolderRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void saveSaving() throws Exception {
        //accountDTO
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();
        accountHolderDTO.setName("Pitin");
        accountHolderDTO.setPassword("111");
        accountHolderDTO.setUsername("acuarela");
        accountHolderDTO.setDateOfBirth("1990-02-02");
        accountHolderDTO.setMailAddress("ñalskdjf");
        accountHolderDTO.setPrimaryAddress(address);
        //savingDTO
        SavingDTO savingDTO = new SavingDTO();
        savingDTO.setBalance(new Money(new BigDecimal(111)));
        savingDTO.setPrimaryOwner(accountHolderDTO);
        savingDTO.setSecretKey("111");

        String body = objectMapper.writeValueAsString(savingDTO);
        MvcResult result = mockMvc.perform(post("/api/savings").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        assertEquals(2, savingsRepository.findAll().size());

    }

    @Test
    @WithMockUser(username = "Pi", password = "111", roles = "THIRD_PARTY")
    void getBalanceById() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/savings/{id}", savings.getId()))
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("300"));
    }

    @Test
    void updateBalance() throws Exception {
        BalanceOnlyDTO balanceOnlyDTO = new BalanceOnlyDTO();
        balanceOnlyDTO.setBalance(new Money(new BigDecimal(400)));

        String body = objectMapper.writeValueAsString(balanceOnlyDTO);
        MvcResult result = mockMvc.perform(patch("/api/savings/{id}", savings.getId())
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    void deleteSavings() throws Exception {
        MvcResult result = mockMvc.perform(delete("/api/savings/{id}", savings.getId()))
                .andExpect(status().isNoContent()).andReturn();
        assertEquals(0, savingsRepository.findAll().size());
    }
}