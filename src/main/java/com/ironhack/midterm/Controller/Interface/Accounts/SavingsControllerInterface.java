package com.ironhack.midterm.Controller.Interface.Accounts;

import com.ironhack.midterm.DTO.AccountsDTO.BalanceOnlyDTO;
import com.ironhack.midterm.DTO.AccountsDTO.SavingDTO;
import com.ironhack.midterm.Model.Class.Money;

import java.util.List;

public interface SavingsControllerInterface {

    void saveSaving(SavingDTO savingDTO) throws Exception;
    Money getBalanceById(int id);
    void updateBalance(int id, BalanceOnlyDTO balanceOnlyDTO);

    void deleteSavings(int id);
}
