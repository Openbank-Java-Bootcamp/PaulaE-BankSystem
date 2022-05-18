package com.ironhack.midterm.Controller.Interface.Accounts;

import com.ironhack.midterm.DTO.AccountsDTO.BalanceOnlyDTO;
import com.ironhack.midterm.DTO.AccountsDTO.CheckingDTO;
import com.ironhack.midterm.Model.Class.Money;

import java.util.List;

public interface CheckingControllerInterface {

    void saveChecking(CheckingDTO checking) throws Exception;


    Money getBalanceById(int id);



    void updateBalance(int id, BalanceOnlyDTO balanceOnlyDTO);

    void deleteChecking(int id);
}
