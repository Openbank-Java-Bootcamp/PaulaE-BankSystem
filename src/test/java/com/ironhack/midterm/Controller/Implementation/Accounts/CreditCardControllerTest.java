package com.ironhack.midterm.Controller.Implementation.Accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm.DTO.AccountsDTO.BalanceOnlyDTO;
import com.ironhack.midterm.DTO.AccountsDTO.CreditCardDTO;
import com.ironhack.midterm.DTO.UsersDTO.AccountHolderDTO;
import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Accounts.CreditCard;
import com.ironhack.midterm.Model.Class.Address;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Model.Users.Role;
import com.ironhack.midterm.Model.Users.ThirdParty;
import com.ironhack.midterm.Repository.Accounts.CheckingRepository;
import com.ironhack.midterm.Repository.Accounts.CreditCardRepository;
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
class CreditCardControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

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
    CreditCard creditCard;

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
                new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"), address, "a??lkdsf","111", "pi" );
        accountHolderRepository.save(accountHolder);

        //creditcard
        creditCard = new CreditCard(new Money(new BigDecimal(300)),accountHolder, null, null, null );
        creditCardRepository.save(creditCard);
    }

    @AfterEach
    void tearDown() {
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void saveCreditCard_WORKS() throws Exception{
        //accountDTO
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();
        accountHolderDTO.setName("Pitin");
        accountHolderDTO.setPassword("111");
        accountHolderDTO.setUsername("acuarela");
        accountHolderDTO.setDateOfBirth("1990-02-02");
        accountHolderDTO.setMailAddress("??alskdjf");
        accountHolderDTO.setPrimaryAddress(address);

        //creditCardDTO
        CreditCardDTO creditCardDTO = new CreditCardDTO();
        creditCardDTO.setBalance(new Money(new BigDecimal(300)));
        creditCardDTO.setPrimaryOwner(accountHolderDTO);

        String body = objectMapper.writeValueAsString(creditCardDTO);
        MvcResult result = mockMvc.perform(post("/api/creditcards").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        assertEquals(2, creditCardRepository.findAll().size());
        assertEquals(2, accountHolderRepository.findAll().size());

    }

    @WithMockUser(username = "Pi", password = "111", roles = "ADMIN")
    @Test
    public void getBalanceById_WORKS() throws Exception{
        MvcResult result = mockMvc.perform(get("/api/creditcards/{id}", creditCard.getId()))
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("300"));
    }

    @Test
    public void updateBalance_WORKS() throws Exception{
        BalanceOnlyDTO balanceOnlyDTO = new BalanceOnlyDTO();
        balanceOnlyDTO.setBalance(new Money(new BigDecimal(400)));

        String body = objectMapper.writeValueAsString(balanceOnlyDTO);
        MvcResult result = mockMvc.perform(patch("/api/creditcards/{id}", creditCard.getId())
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    public void deleteCreditCard_WORKS() throws Exception{
        MvcResult result = mockMvc.perform(delete("/api/creditcards/{id}", creditCard.getId()))
                .andExpect(status().isNoContent()).andReturn();
        assertEquals(0, creditCardRepository.findAll().size());
    }
}