package com.ironhack.midterm.Controller.Interface.Accounts;

import com.ironhack.midterm.DTO.AccountsDTO.BalanceOnlyDTO;
import com.ironhack.midterm.DTO.AccountsDTO.CreditCardDTO;

import java.util.List;

public interface CreditCardControllerInterface {

    void saveCreditCard(CreditCardDTO creditCardDTO) throws Exception;
    List<Object> getBalanceById(int id);
    void updateBalance(int id, BalanceOnlyDTO balanceOnlyDTO);

    void deleteCreditCard(int id);
}
