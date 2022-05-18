package com.ironhack.midterm.Service.Implementation.Accounts;

import com.ironhack.midterm.DTO.AccountsDTO.CheckingDTO;
import com.ironhack.midterm.DTO.AccountsDTO.SavingDTO;
import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Accounts.CreditCard;
import com.ironhack.midterm.Model.Accounts.Savings;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Repository.Accounts.SavingsRepository;
import com.ironhack.midterm.Service.Interface.Accounts.SavingServiceInterface;
import com.ironhack.midterm.Service.Interface.Users.AccountHolderServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class SavingsService implements SavingServiceInterface {

    @Autowired
    AccountHolderServiceInterface accountHolderService;

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveSaving(SavingDTO saving) throws Exception {
        AccountHolder primaryOwner = accountHolderService.saveAccountHolder(saving.getPrimaryOwner());
        AccountHolder secondaryOwner = accountHolderService.saveAccountHolder(saving.getSecondaryOwner());
        String keyEncode = passwordEncoder.encode(saving.getSecretKey());
        Savings newSavings = new Savings(saving.getBalance(), primaryOwner, secondaryOwner,
                keyEncode, saving.getInterestRate());
        savingsRepository.save(newSavings);
    }

    public Money getBalanceById(int id) {
        Optional<Savings> savingsDB = savingsRepository.findById(id);
        if (savingsDB.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        } else {
            savingsDB.get().interestRate();
            savingsRepository.save(savingsDB.get());
            return savingsDB.get().getBalance();
        }
    }


    public void updateBalance(int id, Money Balance){
        Optional<Savings> savingsDB = savingsRepository.findById(id);
        if (savingsDB.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            savingsDB.get().setBalance(Balance);
            savingsRepository.save(savingsDB.get());
        }
    }

    public void deleteSavings(int id) {
        Optional<Savings> savingsDB = savingsRepository.findById(id);
        if (savingsDB.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            boolean deletePO = accountHolderService.CheckTodeleteAccountHolder(savingsDB.get().getPrimaryOwner());
            boolean deleteSO = accountHolderService.CheckTodeleteAccountHolder(savingsDB.get().getSecondaryOwner());
            savingsRepository.deleteById(savingsDB.get().getId());
            if (deletePO){
                accountHolderService.deleteAccountHolder(savingsDB.get().getPrimaryOwner());
            }
            if (deleteSO){
                accountHolderService.deleteAccountHolder(savingsDB.get().getSecondaryOwner());
            }
        }
    }

    public boolean UserAccount(int id, String username){
        boolean gooAccount = false;
        Optional<Savings> savingsDB = savingsRepository.findById(id);
        if (savingsDB.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            List<Savings> userAccounts = savingsRepository.findbyUserName(username);
            for (Savings c : userAccounts){
                if (c.getId() == id){
                    gooAccount = true;
                }
            }
        }
        return gooAccount;
    }


}
