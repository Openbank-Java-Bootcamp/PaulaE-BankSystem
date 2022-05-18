package com.ironhack.midterm.Service.Implementation.Accounts;

import com.ironhack.midterm.DTO.AccountsDTO.CheckingDTO;
import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Accounts.Savings;
import com.ironhack.midterm.Model.Accounts.StudentChecking;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Repository.Accounts.StudentsCheckingRepository;
import com.ironhack.midterm.Repository.Users.RoleRepository;
import com.ironhack.midterm.Service.Interface.Accounts.StudentsCheckingServiceInterface;
import com.ironhack.midterm.Service.Interface.Users.AccountHolderServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class StudentsCheckingService implements StudentsCheckingServiceInterface {
    @Autowired
    StudentsCheckingRepository studentsCheckingRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AccountHolderServiceInterface accountHolderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveStudentChecking(CheckingDTO studentChecking) throws Exception{
        AccountHolder primaryOwner = accountHolderService.saveAccountHolder(studentChecking.getPrimaryOwner());
        AccountHolder secondaryOwner = accountHolderService.saveAccountHolder(studentChecking.getSecondaryOwner());
        String keyEncode =  passwordEncoder.encode(studentChecking.getSecretKey());
        StudentChecking newChecking = new StudentChecking(studentChecking.getBalance(), primaryOwner, secondaryOwner,keyEncode);
        studentsCheckingRepository.save(newChecking);
    }

    public List<Object> getBalanceById(int id){
        Optional<StudentChecking> studentcheckingDB = studentsCheckingRepository.findById(id);
        if (studentcheckingDB.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            return studentsCheckingRepository.findBalanceById(id);
        }
    }

    public void updateBalance(int id, Money Balance){
        Optional<StudentChecking> studentcheckingDB = studentsCheckingRepository.findById(id);
        if (studentcheckingDB.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            studentcheckingDB.get().setBalance(Balance);
            studentsCheckingRepository.save(studentcheckingDB.get());
        }
    }

    public void deleteStudentChecking(int id) {
        Optional<StudentChecking> studentcheckingDB = studentsCheckingRepository.findById(id);
        if (studentcheckingDB.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            boolean deletePO = accountHolderService.CheckTodeleteAccountHolder(studentcheckingDB.get().getPrimaryOwner());
            boolean deleteSO = accountHolderService.CheckTodeleteAccountHolder(studentcheckingDB.get().getSecondaryOwner());
            studentsCheckingRepository.deleteById(studentcheckingDB.get().getId());
            if (deletePO){
                accountHolderService.deleteAccountHolder(studentcheckingDB.get().getPrimaryOwner());
            }
            if (deleteSO){
                accountHolderService.deleteAccountHolder(studentcheckingDB.get().getSecondaryOwner());
            }
        }
    }


    public boolean UserAccount(int id, String username){
        boolean gooAccount = false;
        Optional<StudentChecking> studentcheckingDB = studentsCheckingRepository.findById(id);
        if (studentcheckingDB.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            List<StudentChecking> userAccounts = studentsCheckingRepository.findbyUserName(username);
            for (StudentChecking c : userAccounts){
                if (c.getId() == id){
                    gooAccount = true;
                }
            }
        }
        return gooAccount;
    }

}

