package com.ironhack.midterm.Controller.Implementation.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm.DTO.UsersDTO.ThridPartyDTO;
import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Class.Address;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Model.Users.Admin;
import com.ironhack.midterm.Model.Users.Role;
import com.ironhack.midterm.Model.Users.ThirdParty;
import com.ironhack.midterm.Repository.Accounts.CheckingRepository;
import com.ironhack.midterm.Repository.Users.AccountHolderRepository;
import com.ironhack.midterm.Repository.Users.AdminRepository;
import com.ironhack.midterm.Repository.Users.RoleRepository;
import com.ironhack.midterm.Repository.Users.ThirdPartyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ThirdPartyControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    Collection<Role> roles;
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

        //thridparty
        ThirdParty thirdParty = new ThirdParty("Carla",roles, "111", "111", "Carls");
        thirdPartyRepository.save(thirdParty);

        Address address = new Address("Narbutta", "Madrid", "Spain", "000");
        AccountHolder accountHolder = new AccountHolder("Charo", roles,
                new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"), address, "añlkdsf","111", "pi" );
        accountHolderRepository.save(accountHolder);
        checking = new Checking(new Money(new BigDecimal(300)),accountHolder, null, "111" );
        checkingRepository.save(checking);
    }

    @AfterEach
    void tearDown() {
        checkingRepository.deleteAll();
        accountHolderRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void saveThirdParty_WORK() throws Exception{
        ThridPartyDTO thridPartyDTO = new ThridPartyDTO();
        thridPartyDTO.setName("Pau");
        thridPartyDTO.setPassword("111");
        thridPartyDTO.setUsername("pau");
        thridPartyDTO.setHashedKey("111");

        String body = objectMapper.writeValueAsString(thridPartyDTO);
        MvcResult result = mockMvc.perform(post("/api/thirdparties").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
    }

    @Test
    public void deleteThridParty_WORK() throws Exception{
        ThirdParty thirdParty = new ThirdParty("Pau",roles, "111", "111", "pinpa");
        thirdPartyRepository.save(thirdParty);

        MvcResult result = mockMvc.perform(delete("/api/thirdparties/{id}", thirdParty.getId()))
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    @WithMockUser(username = "Carls", password = "111", roles = "THIRD_PARTY")
    public void transferFunds_WORKS() throws Exception{
        MvcResult result = mockMvc.perform(patch("/api/thridparty/transferfund").queryParam("haskey","111")
                .queryParam("secretkey","111").queryParam("id",String.valueOf(checking.getId())).queryParam("amouth", "111"))
                .andExpect(status().isOk()).andReturn();
    }


}