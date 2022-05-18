package com.ironhack.midterm.Service.Interface.Accounts;

import com.ironhack.midterm.DTO.AccountsDTO.SavingDTO;
import com.ironhack.midterm.Model.Class.Money;

import java.util.List;

public interface SavingServiceInterface {

void saveSaving(SavingDTO saving) throws Exception;
    Money getBalanceById(int id);
    void updateBalance(int id, Money Balance);

    void deleteSavings(int id);

    boolean UserAccount(int id, String username);
}
