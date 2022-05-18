package com.ironhack.midterm.Service.Interface.Users;

import com.ironhack.midterm.DTO.UsersDTO.AccountHolderDTO;
import com.ironhack.midterm.Model.Users.AccountHolder;

import java.math.BigDecimal;

public interface AccountHolderServiceInterface {

    AccountHolder saveAccountHolder(AccountHolderDTO accountHolder) throws  Exception;

    boolean CheckTodeleteAccountHolder(AccountHolder accountHolder);

    void deleteAccountHolder(AccountHolder accountHolder);

    boolean OriginAccountForTransferFund(String username, String owner, BigDecimal amount);
    void tranferFundDestinationAccount(int id, BigDecimal amount);
}
