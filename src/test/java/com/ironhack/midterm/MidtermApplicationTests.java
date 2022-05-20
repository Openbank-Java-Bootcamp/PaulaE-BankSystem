package com.ironhack.midterm;

import com.ironhack.midterm.Service.Implementation.Accounts.CheckingService;
import com.ironhack.midterm.Service.Implementation.Accounts.CreditCardService;
import com.ironhack.midterm.Service.Implementation.Accounts.SavingsService;
import com.ironhack.midterm.Service.Implementation.Accounts.StudentsCheckingService;
import com.ironhack.midterm.Service.Implementation.Users.AccountHolderService;
import com.ironhack.midterm.Service.Implementation.Users.AdminService;
import com.ironhack.midterm.Service.Implementation.Users.RoleService;
import com.ironhack.midterm.Service.Implementation.Users.ThirdPartyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class MidtermApplicationTests {

	@Test
	void contextLoads() {
	}


}
