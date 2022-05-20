package com.ironhack.midterm.Controller.Implementation.Accounts;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm.DTO.AccountsDTO.BalanceOnlyDTO;
import com.ironhack.midterm.DTO.AccountsDTO.CheckingDTO;
import com.ironhack.midterm.DTO.UsersDTO.AccountHolderDTO;
import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Class.Address;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Model.Users.Role;
import com.ironhack.midterm.Model.Users.ThirdParty;
import com.ironhack.midterm.Repository.Accounts.CheckingRepository;
import com.ironhack.midterm.Repository.Accounts.StudentsCheckingRepository;
import com.ironhack.midterm.Repository.Users.AccountHolderRepository;
import com.ironhack.midterm.Repository.Users.RoleRepository;
import com.ironhack.midterm.Repository.Users.ThirdPartyRepository;
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
class CheckingControllerTest {


    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    StudentsCheckingRepository studentsCheckingRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    Collection<Role> roles;
    AccountHolder accountHolder;
    ThirdParty thirdParty;
    Address address;
    Checking checking;



    @BeforeEach
    void setUp() throws Exception {

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

        //thridparty
        thirdParty = new ThirdParty("Carla",roles, "111", "111", "Carls");
        thirdPartyRepository.save(thirdParty);

        //account
        checking = new Checking(new Money(new BigDecimal(300)),accountHolder, null, "111" );
        checkingRepository.save(checking);
    }

    @AfterEach
    void tearDown() {
        studentsCheckingRepository.deleteAll();
        checkingRepository.deleteAll();
        accountHolderRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        roleRepository.deleteAll();
    }

    //checking account
    @Test
    public void saveChecking_WORKS() throws Exception{
        //accountDTO
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();
        accountHolderDTO.setName("Pitin");
        accountHolderDTO.setPassword("111");
        accountHolderDTO.setUsername("acuarela");
        accountHolderDTO.setDateOfBirth("1990-02-02");
        accountHolderDTO.setMailAddress("ñalskdjf");
        accountHolderDTO.setPrimaryAddress(address);
        //checkingDTO
        CheckingDTO checkingDTO = new CheckingDTO();
        checkingDTO.setBalance(new Money(new BigDecimal(111)));
        checkingDTO.setPrimaryOwner(accountHolderDTO);
        checkingDTO.setSecretKey("1111");

        String body = objectMapper.writeValueAsString(checkingDTO);
        MvcResult result = mockMvc.perform(post("/api/checkings").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        assertEquals(2, checkingRepository.findAll().size());
        assertEquals(2, accountHolderRepository.findAll().size());
    }

    //student checking account
    @Test
    public void saveStudentChecking_WORKS() throws Exception{
        //accountDTO
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();
        accountHolderDTO.setName("Carla");
        accountHolderDTO.setPassword("111");
        accountHolderDTO.setUsername("Carlita");
        accountHolderDTO.setDateOfBirth("2000-02-02");
        accountHolderDTO.setMailAddress("ñalskdjf");
        accountHolderDTO.setPrimaryAddress(address);
        //checkingDTO
        CheckingDTO checkingDTO = new CheckingDTO();
        checkingDTO.setBalance(new Money(new BigDecimal(111)));
        checkingDTO.setPrimaryOwner(accountHolderDTO);
        checkingDTO.setSecretKey("1111");

        String body = objectMapper.writeValueAsString(checkingDTO);
        MvcResult result = mockMvc.perform(post("/api/checkings").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        assertEquals(1, studentsCheckingRepository.findAll().size());
        assertEquals(2, accountHolderRepository.findAll().size());
    }

    @WithMockUser(username = "Pi", password = "111", roles = "ADMIN")
    @Test
    public void getBalanceById_WORKS() throws Exception{
        MvcResult result = mockMvc.perform(get("/api/checkings/{id}", checking.getId()))
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("300"));
    }

    @Test
    public void updateBalance_WORKS() throws Exception{
        BalanceOnlyDTO balanceOnlyDTO = new BalanceOnlyDTO();
        balanceOnlyDTO.setBalance(new Money(new BigDecimal(400)));

        String body = objectMapper.writeValueAsString(balanceOnlyDTO);
        MvcResult result = mockMvc.perform(patch("/api/checkings/{id}", checking.getId())
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    public void deleteChecking_WORKS() throws Exception{
        MvcResult result = mockMvc.perform(delete("/api/checkings/{id}", checking.getId()))
                .andExpect(status().isNoContent()).andReturn();
        assertEquals(0, checkingRepository.findAll().size());
    }
}