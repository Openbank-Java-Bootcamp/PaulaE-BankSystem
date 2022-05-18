package com.ironhack.midterm.Service.Interface.Accounts;

import com.ironhack.midterm.DTO.AccountsDTO.CheckingDTO;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CheckingServiceInterface {

    void saveChecking(CheckingDTO Checking) throws Exception;

    List<Object> getBalanceById(int id);
    void updateBalance(int id, Money Balance);

    void deleteChecking(int id);
    boolean UserAccount(int id, String username);
}
