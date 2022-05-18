package com.ironhack.midterm.Service.Implementation.Accounts;


import com.ironhack.midterm.DTO.AccountsDTO.CheckingDTO;
import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Repository.Accounts.CheckingRepository;
import com.ironhack.midterm.Repository.Users.RoleRepository;
import com.ironhack.midterm.Service.Interface.Accounts.CheckingServiceInterface;
import com.ironhack.midterm.Service.Interface.Users.AccountHolderServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CheckingService implements CheckingServiceInterface {

    @Autowired
    CheckingRepository checkingRepository;


    @Autowired
    AccountHolderServiceInterface accountHolderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveChecking(CheckingDTO Checking) throws Exception{
       AccountHolder primaryOwner = accountHolderService.saveAccountHolder(Checking.getPrimaryOwner());
       AccountHolder secondaryOwner = accountHolderService.saveAccountHolder(Checking.getSecondaryOwner());
       String keyEncode =  passwordEncoder.encode(Checking.getSecretKey());
       Checking newChecking = new Checking(Checking.getBalance(), primaryOwner, secondaryOwner, keyEncode);
       checkingRepository.save(newChecking);
    }

    public List<Object> getBalanceById(int id){
        Optional<Checking> checkingDB = checkingRepository.findById(id);
        if (checkingDB.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            return checkingRepository.findBalanceById(id);
        }
    }

    public void updateBalance(int id, Money Balance){
        Optional<Checking> checkingDB = checkingRepository.findById(id);
        if (checkingDB.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            checkingDB.get().setBalance(Balance);
            checkingRepository.save(checkingDB.get());
        }
    }


    public void deleteChecking(int id) {
        Optional<Checking> checkingDB = checkingRepository.findById(id);
        if (checkingDB.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            boolean deletePO = accountHolderService.CheckTodeleteAccountHolder(checkingDB.get().getPrimaryOwner());
            boolean deleteSO = accountHolderService.CheckTodeleteAccountHolder(checkingDB.get().getSecondaryOwner());
            checkingRepository.deleteById(checkingDB.get().getId());
            if (deletePO){
                accountHolderService.deleteAccountHolder(checkingDB.get().getPrimaryOwner());
            }
            if (deleteSO){
                accountHolderService.deleteAccountHolder(checkingDB.get().getSecondaryOwner());
            }
        }
    }

    public boolean UserAccount(int id, String username){
        boolean gooAccount = false;
        Optional<Checking> checkingDB = checkingRepository.findById(id);
        if (checkingDB.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            List<Checking> userAccounts = checkingRepository.findbyUserName(username);
            for (Checking c : userAccounts){
                if (c.getId() == id){
                    gooAccount = true;
                }
            }
        }
        return gooAccount;
    }
}

