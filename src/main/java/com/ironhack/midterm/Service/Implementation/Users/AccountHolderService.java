package com.ironhack.midterm.Service.Implementation.Users;

import com.ironhack.midterm.DTO.UsersDTO.AccountHolderDTO;
import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Accounts.CreditCard;
import com.ironhack.midterm.Model.Accounts.Savings;
import com.ironhack.midterm.Model.Accounts.StudentChecking;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Model.Users.Role;
import com.ironhack.midterm.Model.Users.ThirdParty;
import com.ironhack.midterm.Model.Users.User;
import com.ironhack.midterm.Repository.Accounts.CheckingRepository;
import com.ironhack.midterm.Repository.Accounts.CreditCardRepository;
import com.ironhack.midterm.Repository.Accounts.SavingsRepository;
import com.ironhack.midterm.Repository.Accounts.StudentsCheckingRepository;
import com.ironhack.midterm.Repository.Users.AccountHolderRepository;
import com.ironhack.midterm.Repository.Users.RoleRepository;
import com.ironhack.midterm.Repository.Users.ThirdPartyRepository;
import com.ironhack.midterm.Repository.Users.UserRepository;
import com.ironhack.midterm.Service.Interface.Users.AccountHolderServiceInterface;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AccountHolderService implements AccountHolderServiceInterface {
    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    StudentsCheckingRepository studentsCheckingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean CheckTodeleteAccountHolder(AccountHolder accountHolder) {
        //first we check if the accountHolder has any other account. If not we delete it.
        boolean deleteAccountHolder = false;
        if (accountHolder != null) {
            int holder_total = 0;
            if (checkingRepository.findAll().size() != 0) {
                int holder_checking = checkingRepository.findNumberAccountsByUserid(accountHolder.getId());
                holder_total += holder_checking;
            }
            if (studentsCheckingRepository.findAll().size() != 0) {
                int holder_stchecking = studentsCheckingRepository.findNumberAccountsByUserid(accountHolder.getId());
                holder_total += holder_stchecking;
            }
            if (savingsRepository.findAll().size() != 0) {
                int holder_saving = savingsRepository.findNumberAccountsByUserid(accountHolder.getId());
                holder_total += holder_saving;
            }
            if (creditCardRepository.findAll().size() != 0) {
                int holder_creditcard = creditCardRepository.findNumberAccountsByUserid(accountHolder.getId());
                holder_total += holder_creditcard;
            }
            if (holder_total == 1) {
                deleteAccountHolder = true;
            }
        }
        return deleteAccountHolder;
    }

    public void deleteAccountHolder(AccountHolder accountHolder) {
        accountHolderRepository.delete(accountHolder);
    }

    public AccountHolder saveAccountHolder(AccountHolderDTO accountHolder) throws Exception {
        Optional<AccountHolder> accountHolderExits;
        User userSameUserName = null;
        if (accountHolder == null) {
            return null;
        } else {
            if (userRepository.findAll().size() != 0){
                 userSameUserName = userRepository.findByUsername(accountHolder.getUsername());
            }
            try {
                accountHolderExits = accountHolderRepository.findByDateOfBrithAndPrimaryAddressAndName(
                        new SimpleDateFormat("yyyy-MM-dd").parse(accountHolder.getDateOfBirth()), accountHolder.getPrimaryAddress(), accountHolder.getName());
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The date of birth is not correct try: yyyy-MM-dd");
            }
            AccountHolder newAccountHolder;
            if (accountHolderExits.isPresent()) {
                newAccountHolder = accountHolderExits.get();
                if (userSameUserName == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "That´s not your user name");
                }
            } else {
                //check if the role is already created
                if (userSameUserName != null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username already in use try other.");
                }
                    Optional<Role> role = roleRepository.findByName("ACCOUNT_HOLDER");
                    Collection<Role> roles = new ArrayList<>();
                    if (role.isEmpty()) {
                        Role r = new Role("ACCOUNT_HOLDER");
                        roles.add(r);
                        roleRepository.save(r);
                    } else {
                        roles.add(role.get());
                    }
                    String keyEncode = passwordEncoder.encode(accountHolder.getPassword());
                    newAccountHolder = new AccountHolder(accountHolder.getName(), roles, new SimpleDateFormat("yyyy-MM-dd").parse(accountHolder.getDateOfBirth()),
                            accountHolder.getPrimaryAddress(), accountHolder.getMailAddress(), keyEncode, accountHolder.getUsername());

                    accountHolderRepository.save(newAccountHolder);
                }
                return newAccountHolder;
            }
    }

    public boolean OriginAccountForTransferFund(String username, String owner, BigDecimal amount){
        boolean OriginAccountFound = false;
        //FINDING THE MAX BALANCE
            String typoAccount = null;
            int idMaxBalance = 0;
            BigDecimal maxBalance = new BigDecimal(0);
            List<Checking> chekingValid = checkingRepository.findBalanceByUserNameAndNameAndAmount(username, owner, amount);
            if (chekingValid.size() != 0) {
                for (Checking o : chekingValid){
                    if (o.getBalance().getAmount().compareTo(maxBalance) > 0){
                        maxBalance = o.getBalance().getAmount();
                        idMaxBalance = o.getId();
                        typoAccount = "CHECKING";
                    }
                }
            }
            List<CreditCard> creditCardValid = creditCardRepository.findBalanceByUserNameAndNameAndAmount(username, owner, amount);
            if (creditCardValid.size() != 0) {
                for (CreditCard o : creditCardValid){
                    if (o.getBalance().getAmount().compareTo(maxBalance) > 0){
                        maxBalance = o.getBalance().getAmount();
                        idMaxBalance = o.getId();
                        typoAccount = "CREDITCARD";
                    }
                }
            }
            List<Savings> savingsValid = savingsRepository.findBalanceByUserNameAndNameAndAmount(username, owner, amount);
            if (savingsValid.size() != 0) {
                for (Savings o : savingsValid){
                    if (o.getBalance().getAmount().compareTo(maxBalance) > 0){
                        maxBalance = o.getBalance().getAmount();
                        idMaxBalance = o.getId();
                        typoAccount = "SAVINGS";
                    }
                }
            }
            List<StudentChecking> studentCheckingsValid = studentsCheckingRepository.findBalanceByUserNameAndNameAndAmount(username, owner, amount);
            if (studentCheckingsValid.size() != 0) {
                for (StudentChecking o : studentCheckingsValid){
                    if (o.getBalance().getAmount().compareTo(maxBalance) > 0){
                        maxBalance = o.getBalance().getAmount();
                        idMaxBalance = o.getId();
                        typoAccount = "STUDENTCHECKING";
                    }
                }
            }
            //DECREASING THE AMOUNTH FROM THE ONE WITH HIGHEST BALANCE
            switch (typoAccount){
                case "CHECKING":
                    Checking account = checkingRepository.findById(idMaxBalance).get();
                    account.getBalance().decreaseAmount(amount);
                    account.penaltyFee();
                    checkingRepository.save(account);
                    OriginAccountFound = true;
                    break;
                case "CREDITCARD":
                    CreditCard account1 = creditCardRepository.findById(idMaxBalance).get();
                    account1.getBalance().decreaseAmount(amount);
                    creditCardRepository.save(account1);
                    OriginAccountFound = true;
                    break;
                case "SAVINGS":
                    Savings account3 = savingsRepository.findById(idMaxBalance).get();
                    account3.getBalance().decreaseAmount(amount);
                    account3.penaltyFee();
                    savingsRepository.save(account3);
                    OriginAccountFound = true;
                    break;
                case "STUDENTCHECKING":
                    StudentChecking account4 = studentsCheckingRepository.findById(idMaxBalance).get();
                    account4.getBalance().decreaseAmount(amount);
                    studentsCheckingRepository.save(account4);
                    OriginAccountFound = true;
                    break;
                default:
                    OriginAccountFound = false;
            }
            return OriginAccountFound;
        }
    public void tranferFundDestinationAccount(int id, BigDecimal amount){
            Optional<Checking> checkingDB = checkingRepository.findById(id);
            Optional<CreditCard> creditcardDB = creditCardRepository.findById(id);
            Optional<Savings> savingsDB = savingsRepository.findById(id);
            Optional<StudentChecking> studentcheckingDB = studentsCheckingRepository.findById(id);
            if (checkingDB.isPresent()){
                checkingDB.get().getBalance().increaseAmount(amount);
                checkingRepository.save(checkingDB.get());
            }else if(creditcardDB.isPresent()){
                creditcardDB.get().getBalance().increaseAmount(amount);
                creditCardRepository.save(creditcardDB.get());
            }else if(savingsDB.isPresent()){
                savingsDB.get().getBalance().increaseAmount(amount);
                savingsRepository.save(savingsDB.get());
            }else if (studentcheckingDB.isPresent()){
                studentcheckingDB.get().getBalance().increaseAmount(amount);
                studentsCheckingRepository.save(studentcheckingDB.get());
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is not an account with that ID to send the funds to.");
            }
    }

    public void tranferFundTP(String name, BigDecimal amount){
        Optional<ThirdParty> thirdPartyDB = thirdPartyRepository.findByName(name);
        if (thirdPartyDB.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "That name doesn´t correspond to a Third Party");
        }
    }

    public boolean accountIdExist(int id){
        Optional<Checking> checkingDB = checkingRepository.findById(id);
        Optional<CreditCard> creditcardDB = creditCardRepository.findById(id);
        Optional<Savings> savingsDB = savingsRepository.findById(id);
        Optional<StudentChecking> studentcheckingDB = studentsCheckingRepository.findById(id);
        if (checkingDB.isPresent()){
            return true;
        }else if(creditcardDB.isPresent()){
            return true;
        }else if(savingsDB.isPresent()){
            return true;
        }else if (studentcheckingDB.isPresent()){
            return true;
        }else{
            return false;
        }
    }



}
