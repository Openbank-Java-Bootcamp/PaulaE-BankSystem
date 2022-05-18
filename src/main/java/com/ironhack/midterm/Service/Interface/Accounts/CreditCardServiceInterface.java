package com.ironhack.midterm.Service.Interface.Accounts;

import com.ironhack.midterm.DTO.AccountsDTO.CreditCardDTO;
import com.ironhack.midterm.Model.Class.Money;

import java.util.List;


public interface CreditCardServiceInterface  {

     void saveCreditCard( CreditCardDTO creditCardDTO) throws Exception;

     Money getBalanceById(int id);
     void updateBalance(int id, Money Balance);

     void deleteCreditCard(int id);

     boolean UserAccount(int id, String username);
}
