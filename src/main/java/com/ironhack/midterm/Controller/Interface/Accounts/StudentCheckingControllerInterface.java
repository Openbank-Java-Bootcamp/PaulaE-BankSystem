package com.ironhack.midterm.Controller.Interface.Accounts;

import com.ironhack.midterm.DTO.AccountsDTO.BalanceOnlyDTO;
import com.ironhack.midterm.Model.Accounts.StudentChecking;

import java.util.List;

public interface StudentCheckingControllerInterface {

    List<Object> getBalanceById(int id);
    void updateBalance(int id, BalanceOnlyDTO balanceOnlyDTO);

    void deleteStudentChecking(int id);

}
