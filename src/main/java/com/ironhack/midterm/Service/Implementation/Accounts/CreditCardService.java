package com.ironhack.midterm.Service.Implementation.Accounts;

import com.ironhack.midterm.DTO.AccountsDTO.CreditCardDTO;
import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Accounts.CreditCard;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Repository.Accounts.CreditCardRepository;
import com.ironhack.midterm.Service.Interface.Accounts.CreditCardServiceInterface;
import com.ironhack.midterm.Service.Interface.Users.AccountHolderServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CreditCardService implements CreditCardServiceInterface {

    @Autowired
    AccountHolderServiceInterface accountHolderService;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveCreditCard( CreditCardDTO creditCardDTO) throws Exception{
        AccountHolder primaryOwner = accountHolderService.saveAccountHolder(creditCardDTO.getPrimaryOwner());
        AccountHolder secondaryOwner = accountHolderService.saveAccountHolder(creditCardDTO.getSecondaryOwner());
        CreditCard newCreditCard = new CreditCard(creditCardDTO.getBalance(), primaryOwner, secondaryOwner,
                creditCardDTO.getCreditLimit(), creditCardDTO.getInterestRate());
        creditCardRepository.save(newCreditCard);
    }

    public Money getBalanceById(int id){
        Optional<CreditCard> creditcardDB = creditCardRepository.findById(id);
        if (creditcardDB.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            creditcardDB.get().interestRate();
            creditCardRepository.save(creditcardDB.get());
            return creditcardDB.get().getBalance();
        }
    }

    public void updateBalance(int id, Money Balance){
        Optional<CreditCard> creditcardDB = creditCardRepository.findById(id);
        if (creditcardDB.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            creditcardDB.get().setBalance(Balance);
            creditCardRepository.save(creditcardDB.get());
        }
    }

    public void deleteCreditCard(int id) {
        Optional<CreditCard> creditcardDB = creditCardRepository.findById(id);
        if (creditcardDB.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            boolean deletePO = accountHolderService.CheckTodeleteAccountHolder(creditcardDB.get().getPrimaryOwner());
            boolean deleteSO = accountHolderService.CheckTodeleteAccountHolder(creditcardDB.get().getSecondaryOwner());
            creditCardRepository.deleteById(creditcardDB.get().getId());
            if (deletePO){
                accountHolderService.deleteAccountHolder(creditcardDB.get().getPrimaryOwner());
            }
            if (deleteSO){
                accountHolderService.deleteAccountHolder(creditcardDB.get().getSecondaryOwner());
            }
        }
    }

    public boolean UserAccount(int id, String username){
        boolean gooAccount = false;
        Optional<CreditCard> creditcardDB = creditCardRepository.findById(id);
        if (creditcardDB.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id does´t not match any account");
        }else {
            List<CreditCard> userAccounts = creditCardRepository.findbyUserName(username);
            for (CreditCard c : userAccounts){
                if (c.getId() == id){
                    gooAccount = true;
                }
            }
        }
        return gooAccount;
    }
}

