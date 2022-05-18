package com.ironhack.midterm;

import com.ironhack.midterm.Model.Accounts.CreditCard;
import com.ironhack.midterm.Service.Implementation.Accounts.CheckingService;
import com.ironhack.midterm.Service.Implementation.Accounts.CreditCardService;
import com.ironhack.midterm.Service.Implementation.Accounts.SavingsService;
import com.ironhack.midterm.Service.Implementation.Accounts.StudentsCheckingService;
import com.ironhack.midterm.Service.Implementation.Users.AccountHolderService;
import com.ironhack.midterm.Service.Implementation.Users.AdminService;
import com.ironhack.midterm.Service.Implementation.Users.RoleService;
import com.ironhack.midterm.Service.Implementation.Users.ThirdPartyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class MidtermApplication {
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(MidtermApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleService roleService, CheckingService checkingService, CreditCardService creditCardService,
						  SavingsService savingService, StudentsCheckingService studentsCheckingService,
						  AccountHolderService accountHolderService, AdminService adminService, ThirdPartyService thirdPartyService) {
		return args -> {
		};
	}


}
