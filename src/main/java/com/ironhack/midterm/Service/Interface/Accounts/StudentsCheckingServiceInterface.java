package com.ironhack.midterm.Service.Interface.Accounts;


import com.ironhack.midterm.DTO.AccountsDTO.CheckingDTO;
import com.ironhack.midterm.Model.Class.Money;

import java.util.List;

public interface StudentsCheckingServiceInterface {

    void saveStudentChecking(CheckingDTO studentChecking) throws Exception;
    List<Object> getBalanceById(int id);
    void updateBalance(int id, Money Balance);

    void deleteStudentChecking(int id);

    boolean UserAccount(int id, String username);
}
