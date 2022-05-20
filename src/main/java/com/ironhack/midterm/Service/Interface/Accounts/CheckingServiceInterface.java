package com.ironhack.midterm.Service.Interface.Accounts;

import com.ironhack.midterm.DTO.AccountsDTO.BalanceOnlyDTO;
import com.ironhack.midterm.DTO.AccountsDTO.CheckingDTO;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CheckingServiceInterface {

    void saveChecking(CheckingDTO Checking) throws Exception;

    Money getBalanceById(int id);
    void updateBalance(int id, BalanceOnlyDTO balanceOnlyDTO);

    void deleteChecking(int id);
    boolean UserAccount(int id, String username);
}
