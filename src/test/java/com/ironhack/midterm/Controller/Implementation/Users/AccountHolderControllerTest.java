package com.ironhack.midterm.Controller.Implementation.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountHolderControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

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
    CreditCard creditCard;
    CreditCard creditCard2;

    Checking checking;

    @BeforeEach
    void setUp() throws  Exception{

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

        //creditcard
        creditCard = new CreditCard(new Money(new BigDecimal(300)),accountHolder, null, null, null );
        creditCardRepository.save(creditCard);

        //creditcard2
        creditCard2 = new CreditCard(new Money(new BigDecimal(150)),accountHolder, null, null, null );
        creditCardRepository.save(creditCard2);

        //thridparty
        thirdParty = new ThirdParty("Carla",roles, "111", "111", "Carls");
        thirdPartyRepository.save(thirdParty);

        //checking
        checking = new Checking(new Money(new BigDecimal(500)),accountHolder, null, "111" );
        checkingRepository.save(checking);
    }

    @AfterEach
    void tearDown() {
        checkingRepository.deleteAll();
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "pi", password = "111", roles = "ACCOUNT_HOLDER")
    void transferfunds_WORKS() throws Exception {
        MvcResult result = mockMvc.perform(patch("/api/accountholder/transferfunds")
                        .queryParam("username", "Charo").queryParam("id",String.valueOf(creditCard2.getId())).queryParam("quantity", "111"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @WithMockUser(username = "pi", password = "111", roles = "ACCOUNT_HOLDER")
    void transferfundsTP_WORKS() throws Exception{
        MvcResult result = mockMvc.perform(patch("/api/accountholder/transferfunds/thridparty")
                        .queryParam("name", "Charo").queryParam("quantity", "111").queryParam("TPname","Carla"))
                .andExpect(status().isOk()).andReturn();
    }
    

}

